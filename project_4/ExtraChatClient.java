import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.math.*;

public class ExtraChatClient extends JFrame implements Runnable, ActionListener {
    
    public static final int PORT = 8765;    // Server port.

    ObjectInputStream myReader;
    ObjectOutputStream myWriter;
    SymCipher cipher;

    JTextArea outputArea;
    JLabel prompt;
    JTextField inputField;
    String myName, serverName, myPassword;
	Socket connection;

    public ExtraChatClient() {
        try {
            serverName = JOptionPane.showInputDialog(this, "Enter the server name: ");  // Prompt the user for the server name.

            InetAddress addr = InetAddress.getByName(serverName);   // Connect to the server
            connection = new Socket(addr, PORT);                    // with a new Socket.

            myWriter = new ObjectOutputStream(connection.getOutputStream());   // Create an ObjectOutputStream on the socket (for writing).
            myWriter.flush();       // Flush the writer.

            myReader = new ObjectInputStream(connection.getInputStream());  // Create an ObjectInputStream on the socket (for reading).

            BigInteger E = (BigInteger) myReader.readObject();     // Receive (read) the server's public key, E, as a BigInteger Object.
            
            BigInteger N = (BigInteger) myReader.readObject();     // Receive (read) the server's public mod value, N, as a BigInteger Object.

            System.out.println("E: " + E.toString()); // Output the keys E and N received from the server to the console.
            System.out.println("N: " + N.toString());

            String encType = (String) myReader.readObject();   // Receive (read) the server's preferred symmetric cipher as a String Object.

            if(encType.equals("Sub")) {     // Output the type of symmetric encryption to the console.
                System.out.println("Symmetric Encryption Type: Substitute");
            }
            else if(encType.equals("Add")) {
                System.out.println("Symmetric Encryption Type: Add128");
            }

            cipher = null;                  // Based on the value of the cipher preference, create either a
            if(encType.equals("Sub")) {     // Substitute object or an Add128 object, storing the resulting
                cipher = new Substitute();  // object in a SymCipher variable.
            }
            else if(encType.equals("Add")) {
                cipher = new Add128();
            }

            byte [] key = cipher.getKey();          // Get the key from the cipher object using the getKey() method,
            BigInteger M = new BigInteger(1, key);  // then convert the result into a BigInteger object.

            BigInteger C = M.modPow(E, N);   // RSA-encrypt the BigInteger version of the key, M, using E and N,
            myWriter.writeObject(C);         // and send the resulting BigInteger, C, to the server.
            myWriter.flush();

            byte [] encodedPassword = (byte []) myReader.readObject();
            String decodedPassword = cipher.decode(encodedPassword);

            myPassword = JOptionPane.showInputDialog(this, "Enter the server password: ");
            while (!myPassword.equals(decodedPassword)) {
                JOptionPane.showMessageDialog(null, "Wrong password!");
                myPassword = JOptionPane.showInputDialog(this, "Enter the server password: ");
                connection.setSoTimeout(0); // To prevent client timeout while entering the password.
            }

            myName = JOptionPane.showInputDialog(this, "Enter your user name: ");   // Prompt the user for their name,
            byte [] encodedName = cipher.encode(myName);                            // then encrypt it using the symmetric cipher and
            myWriter.writeObject(encodedName);                                      // send it to the server.
            myWriter.flush();

            // At this point the “handshaking” is complete and the client begins its regular execution. The regular 
            // execution of the client will involve the following basic abilities:

            this.setTitle(myName);      // Set title to identify chatter.

            Box b = Box.createHorizontalBox();  // Set up graphical environment for
            outputArea = new JTextArea(8, 30);  // user.
            outputArea.setEditable(false);
            b.add(new JScrollPane(outputArea));

            outputArea.append("Welcome to the Chat Group, " + myName + "\n");

            inputField = new JTextField("");  // This is where user will type input.
            inputField.addActionListener(this);

            prompt = new JLabel("Type your messages below:");
            Container c = getContentPane();

            c.add(b, BorderLayout.NORTH);
            c.add(prompt, BorderLayout.CENTER);
            c.add(inputField, BorderLayout.SOUTH);

            Thread outputThread = new Thread(this);  // Thread is to receive strings
            outputThread.start();                    // from Server.

		    addWindowListener(
                    new WindowAdapter()
                    {
                        public void windowClosing(WindowEvent e)
                        {
                            try {
                                String clientClosing = "CLIENT CLOSING";                // Create a String for CLIENT CLOSING,
                                byte [] encodedClosing = cipher.encode(clientClosing);  // then encode it using the symmetric cipher
                                myWriter.writeObject(encodedClosing);                   // and send the resulting byte array to the server.
                                myWriter.flush();
                            }
                            catch (Exception d) {
                                System.out.println(d + ", closing client!");
                            }
                            System.exit(0);
                        }
                    }
                );

        setSize(500, 200);
        setVisible(true);

        }
        catch (Exception e) {
            System.out.println("Problem starting client!");
        }
    }

    // Wait for a message to be received, then show it to the output area:
    public void run() {
        while(true) {
            try {
                byte [] encodedMsg = (byte []) myReader.readObject();    // Read an encrypted byte array from the server,
                String currMsg = cipher.decode(encodedMsg);             // decrypt it,
                outputArea.append(currMsg + "\n");                      // and display the String to the user.

                Thread.sleep(25);   // Sleep to prevent print statements from mixinf with other print statements above.
                // For each message that is decrypted, output the following to the console:
                //      1. The array of bytes received.
                //      2. The decrypted array of bytes.
                //      3. The corresponding String.
                System.out.println("MESSAGE RECEIVED!");
                System.out.println("Array of bytes received: " + Arrays.toString(encodedMsg));
                System.out.println("Decrypted array of bytes: " + Arrays.toString(currMsg.getBytes()));
                System.out.println("Corresponding String: " + currMsg);
            }
            catch (Exception e) {
                System.out.println(e + ", closing client!");
                break;
            }
        }
        System.exit(0);
    }

    // Get a message typed in from the user (from inputField), then add name and send
    // it to the server:
    public void actionPerformed(ActionEvent e) {
        try {
            String currMsg = e.getActionCommand();      // Read a String from the user,
            inputField.setText("");
            currMsg = myName + ": " + currMsg;          // append the user's name to the message,
            byte[] encodedMsg = cipher.encode(currMsg); // encrypt it using the SymCipher,
            myWriter.writeObject(encodedMsg);           // and send it to the server.
            myWriter.flush();

            // For each message that is encrypted, output the following to the console:
            //      1. The original String message.
            //      2. The corresponding array of bytes.
            //      3. The encrypted array of bytes.
            System.out.println("MESSAGE SENT!");
            System.out.println("Original String message: " + currMsg);
            System.out.println("Corresponding array of bytes: " + Arrays.toString(currMsg.getBytes()));
            System.out.println("Encrypted array of bytes: " + Arrays.toString(encodedMsg));
        }
        catch(Exception f) {
            System.out.println(f + ", closing client!");
        }
    }

    // Start things off by creating an ExtraChatClient object:
    public static void main(String [] args)
    {
         ExtraChatClient JR = new ExtraChatClient();
         JR.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
}