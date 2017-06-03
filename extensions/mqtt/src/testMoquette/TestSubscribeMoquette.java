/*
 * Copyright (C) 2017 stefanopiozingaro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package testMoquette;

import io.moquette.Client;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttPublishVariableHeader;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.util.CharsetUtil;

/* TODO 
 * 1. Fare la sottoscrizione (subscribe a topic temperature sulla sandbox)
 * 2. Fare la pubblicazione (QoS2) 
 * 3. Fare la subscribe su topic + publish su topic + stampa subscriber 
 * 4. Serializzare un oggetto in ByteBuf (cerca su Mqtt)
 */
/**
 *
 * @author stefanopiozingaro
 */
public class TestSubscribeMoquette {

    // from http://test.mosquitto.org
    private static final String host = "test.mosquitto.org";
    // from http://test.mosquitto.org/gauge/ 
    private static final String topic = "temp/random";

    public TestSubscribeMoquette() {

        Client c = new Client(host, 1883);

        c.connect();

        /*
        Subscribe callback
         */
        Client.ICallback callback = (MqttMessage msg1) -> {
            System.out.println(msg1.toString());
        };

        c.setCallback(callback);
        /*
        End Subscribe callback
         */

        MqttFixedHeader mqttFixedHeader
                = new MqttFixedHeader(MqttMessageType.SUBSCRIBE,
                        false, MqttQoS.AT_LEAST_ONCE, true, 2);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new TestSubscribeMoquette();
    }
}
