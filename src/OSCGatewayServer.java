/*
 * Copyright (C) 2012 rosendorsc
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 */

/**
 *
 * @author rosendorsc
 */
public class OSCGatewayServer {

    private static Dock dock;

    public OSCGatewayServer() {
        dock = new Dock();
    }

    private static void autostart() {
        dock.getMyGateway().startServers();
    }

    static public void main(String[] args) {

        OSCGatewayServer main = new OSCGatewayServer();
        if (args.length == 1) {
            if (args[0].contains("-autostart")) {
                autostart();
            }
        }
    }
}
