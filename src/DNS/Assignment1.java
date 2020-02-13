package DNS;

/** 
 * Main class for our first assignment.
 * In this assignment, we create a DNS client which sends and receives data from a socket.
 * Arguments must be entered in the following format [-t timeout] [-r max-retries] [-p port] [-mx|-ns] @server name
 * 
 * @author Sophie and Mia
 *
 */
public class Assignment1 {
	static private int timeout = 5000; //time in ms
	static private int maxRetries = 3;
	static private int port = 53;
	static private QueryType queryType = QueryType.A;
	static private String name;
	static private byte[] ipAddress = new byte[4];
	static private String serverAddress; //IPv4 address of the DNS server
	
	//constants
	static private int MAX_INPUT_ARGUMENTS = 9;
	static private int MIN_INPUT_ARGUMENTS = 2;
	static private int IP_SIZE = 4;
	static private int MIN_IP = 0;
	static private int MAX_IP = 255;


	public static void main(String args[]) throws Exception {
		try {
			parseInputArguments(args);
			if(name == null || serverAddress == null) {
				throw new Exception("Please enter server address and name");
			}
			//System.out.println("Timeout: " + timeout + ", max_retries: " + maxRetries + ", port: " + port + ", address: "+ serverAddress + ", name: " + name);

			//Send request and read response through client
			Client client = new Client();
			client.sendRequest(name, serverAddress, queryType, timeout, ipAddress, port, maxRetries);
			//client.readResponse(r);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error: " + e.getMessage());
		}
	}

	private static void parseInputArguments(String args[]) throws Exception {
		int argsLength = args.length;
		if(argsLength < MIN_INPUT_ARGUMENTS || argsLength > MAX_INPUT_ARGUMENTS) {
			throw new Exception("Wrong number of arguments.");
		}
		for(int i = 0; i < argsLength; i++) {
			String arg = args[i];
			if(arg.compareTo("-t") == 0) {
				timeout = Integer.parseInt(args[i+1]) * 1000;
				i++;
			}
			else if(arg.compareTo("-r") == 0) {
				maxRetries = Integer.parseInt(args[i+1]);
				i++;
			}
			else if(arg.compareTo("-p") == 0) {
				port = Integer.parseInt(args[i+1]);
				i++;
			}
			else if(arg.compareTo("-mx") == 0) {
				queryType = QueryType.MX;
			}
			else if(arg.compareTo("-ns") == 0) {
				queryType = QueryType.NS;
			}
			else if(arg.charAt(0) == '@') {
				serverAddress = arg.substring(1);
				String[] serverSplit = serverAddress.split("\\.");
				if(serverSplit.length != IP_SIZE) {
					throw new Exception("Wrong IP Address formatn (a.b.c.d).");
				}
				for(int j = 0; j < IP_SIZE; j++) {
					int ip = Integer.parseInt(serverSplit[j]);
					if (ip < MIN_IP || ip > MAX_IP) {
						throw new Exception("IP Address numbers must be between 0 and 255.");
					}
					ipAddress[j] = (byte) ip;
				}
				name = args[i+1];
			}
		}
	}
}
