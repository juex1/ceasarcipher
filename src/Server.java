import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends JFrame {
    JLabel l1,l2,l3,l,l4;
    Font font =  new Font("Courier New", Font.BOLD, 30);
    Font font2 = new Font("Times New Roman", Font.BOLD, 50);
    Font font3 = new Font("Times New Roman", Font.BOLD, 22);
    JTextField tf;
    JButton b1,b2,b3,ExitClient,ClearCleint;;
    JPanel p1;
    DataInputStream input;
    DataOutputStream output;
    Socket socket;
    private ClearButtonHandler clbHandler;
    private ExitButtonHandler ebHandler;
    String alphabets  = "abcdefghijklmnopqrstuvwxyz";
    String result = "";
    String finEnc = "";
    String finDec = "";

    public Server() throws Exception{
        super.setName("Server");
        setSize(600,500);
        setTitle("Server");
        setLocation(30,50);
        setLayout(null);
        getContentPane().setBackground(Color.decode("#62CDFF"));
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        l=new JLabel("Security");
        l.setBounds(250,10,200,50);
        l.setForeground(Color.decode("#66347F"));
        l.setFont(font2);

        l1 = new JLabel("Enter the text");
        l1.setBounds(250,50,200,50);
        l1.setFont(font3);

        tf = new JTextField(15);
        tf.setBounds(200,120,200,80);

        l2 = new JLabel();
        l2.setBounds(150,20,200,50);
        l2.setSize(125,10);

        l3 = new JLabel();
        l3.setBounds(150,50,200,50);

        l4=new JLabel();
        l4.setBounds(150,80,200,50);


        ServerSocket serverSocket = new ServerSocket(8000);
        Socket socket = serverSocket.accept();
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        b1 = new JButton("send");
        b1.setBackground(Color.decode("#5D9C59"));

        ExitClient=new JButton("Exit");
        ExitClient.setBounds(80,125,100,50);
        ebHandler = new ExitButtonHandler();
        ExitClient.addActionListener(ebHandler);
        ExitClient.setBackground(Color.decode("#19A7CE"));



        b1.setBounds(420,125,100,50);

        b2 = new JButton("Encrypt");
        b2.setBounds(80,250,100,50);
        b2.setBackground(Color.decode("#19A7CE"));
        b2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                l2.setText(finEnc);

            }
        });

        b3 = new JButton("Decryption");
        b3.setBounds(200,250,100,50);
        b3.setBackground(Color.decode("#A4907C"));
        b3.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                l3.setText(finDec);

            }
        });
        ClearCleint=new JButton("Clear All");
        ClearCleint.setBounds(310,250,100,50);
        ClearCleint.setBackground(Color.decode("#A84448"));
        clbHandler = new ClearButtonHandler();
        ClearCleint.addActionListener( clbHandler);

        String  str2=(String)input.readUTF();

        l4.setText(str2);
        finEnc = str2;


        for(int i=0;i<str2.length();i++){
            int pos = alphabets.indexOf(str2.charAt(i));
            int enc = (pos - 3)%26;
            if(enc < 0){
                enc = alphabets.length() + enc;
            }

            char ind = alphabets.charAt(enc);
            result += ind;
            finDec = result;
        }
        //l3.setText(result);

        b1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                    String msg = tf.getText();
                    result = "";
                    for(int i=0;i<msg.length();i++){
                        int pos = alphabets.indexOf(msg.charAt(i));
                        int enc = (pos + 3)%26;
                        char ind = alphabets.charAt(enc);
                        result += ind;
                    }
                    //  l2.setText(str2);
                    output.writeUTF(result);
                    String str3=(String)input.readUTF();
                    serverSocket.close();
                } catch (IOException ex) {
                }

            }
        });



        p1 = new JPanel();
        p1.setBounds(100,330,400,100);
        p1.setBackground(Color.white);
        p1.setLayout(null);
        add(l1);
        add(l);
        add(tf);
        add(b1);
        add(b2);
        add(b3);
        add(ExitClient);
        add(ClearCleint);
        p1.add(l2);
        p1.add(l3);
        p1.add(l4);
        add(p1);

    }
    private class ClearButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            tf.setText("");
            l2.setText("");
            l3.setText("");

        }
    }
    private class ExitButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);//allows the exit button to close the JFrame window
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Server();
                } catch (Exception ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

}

