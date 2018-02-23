include "../AbstractTestUnit.iol"
include "time.iol"

interface HTTPInterface {
RequestResponse:
	default(undefined)(undefined)
}



outputPort HttpsOutput {
  Location:"socket://localhost:14001"
  Protocol: https {
	   .keepAlive = false; // Keep connections open
	   .debug = DebugHttp;
	   .debug.showContent = DebugHttpContent
		 .ssl.keyStore = "./extensions/private/https_leonardo/ssl/KeyStore.jks";
		 .ssl.keyStorePassword ="a4KPCrUxShYsjkhQ"

   }
   Interfaces: HTTPInterface
}

embedded {
Jolie:
	"./extensions/private/leonardo_https.ol" 
}

init{
	sleep@Time(20000)()
}

define doTest
{

	for (counterAttempt = 0 , counterAttempt <10 , counterAttempt++){
    request.operaration="index.html";
		default@HttpsOutput(request)();
		request.operaration="jquery-1.12.2.js";
		default@HttpsOutput(request)()
  }
}
