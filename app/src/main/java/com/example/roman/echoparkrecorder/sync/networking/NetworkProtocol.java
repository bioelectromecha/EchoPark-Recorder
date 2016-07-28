package com.example.roman.echoparkrecorder.sync.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class NetworkProtocol {
	public static final int PORT_NUMBER = 54555;
	public static final String HOTSPOT_IP = "192.168.43.1";
	public static final int TIMEOUT_TIME = 5000;

    public static final int START_COMMAND = 0;
    public static final int STOP_COMMAND = 1;

	// This registers objects that are going to be sent over the network.
	static public void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
        kryo.register(CommandMessage.class);
	}

    static public class CommandMessage {
        private int mCommand;
		private long mServerTime;

		public CommandMessage(int command, long serverTime) {
			mCommand = command;
			mServerTime = serverTime;
		}

		public int getCommand() {
            return mCommand;
        }

		public long getServerTime() {
			return mServerTime;
		}
	}

}