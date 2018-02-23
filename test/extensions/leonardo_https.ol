include "file.iol"
include "string_utils.iol"
include "protocols/http.iol"
include "console.iol"

interface HTTPInterface {
RequestResponse:
	default(DefaultOperationHttpRequest)(undefined)
}




inputPort HttpsInput {
  Location:"socket://localhost:14001"
  Protocol: https {
	   .keepAlive = false; // Keep connections open
	   .debug = DebugHttp;
	   .debug.showContent = DebugHttpContent;
	   .format -> format;
	   .contentType -> mime;
      .statusCode -> statusCode;
	   .default = "default";
		 .ssl.keyStore = "./private/https_leonardo/ssl/KeyStore.jks";
		 .ssl.keyStorePassword ="a4KPCrUxShYsjkhQ"

   }
   Interfaces: HTTPInterface
}

execution{ sequential }
init {
  setMimeTypeFile@File("./private/https_leonardo/META-INF/mime.types")()
}
main{

  [ default( request )( response ) {
		scope( s ) {
			install( FileNotFound => println@Console( "File not found: " + file.filename )() );

			s = request.operation;
			s.regex = "\\?";
			split@StringUtils( s )( s );

			// Default page
			if ( s.result[0] == "" ) {
				s.result[0] = DefaultPage
			};
			pagename = s.result[0];
			file.filename = "./private/https_leonardo/www/" + s.result[0];

			getMimeType@File( file.filename )( mime );
			mime.regex = "/";
			split@StringUtils( mime )( s );
			if ( s.result[0] == "text" ) {
				file.format = "text";
				format = "html"
			} else {
				file.format = format = "binary"
			};

			readFile@File( file )( __content );
			response -> __content
		}
	} ] { nullProcess }
}
