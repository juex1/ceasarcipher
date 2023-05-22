import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends JFrame {
    JLabel l1,l2,l3,l;
    JTextField tf;
    JButton b1,b2,b3,ExitClient,ClearCleint;


    private ClearButtonHandler clbHandler;
    private ExitButtonHandler ebHandler;

    JPanel p1;


    String str = "";
    Socket socket;
    String alphabet  = "ABCDEFGHIGKLMNOPQRSTUVWXYZ";
    String alphabets  = "abcdefghijklmnopqrstuvwxyz";
    String finEnc = "";
    String finDec = "";
    String result = "";
    Font font =  new Font("Courier New", Font.BOLD, 30);
    Font font2 = new Font("Times New Roman", Font.BOLD, 50);
    Font font3 = new Font("Times New Roman", Font.BOLD, 22);
    public Client() throws Exception{


        setSize(600,500);
        setTitle("Client");

        setLocation(650,50);

        setLayout(null);
        getContentPane().setBackground(Color.decode("#AA77FF"));
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        l=new JLabel("Client Side");
        l.setBounds(150,10,300,50);
        l.setForeground(Color.decode("#9E4784"));
        l.setFont(font2);

        l1 = new JLabel("Enter the text");
        l1.setBounds(250,80,200,50);
        l1.setFont(font3);


        tf = new JTextField(10);
        tf.setBounds(200,120,200,80);

        l2 = new JLabel();
        l2.setBounds(150,20,200,50);


        l3 = new JLabel();
        l3.setBounds(150,50,200,50);

        socket = new Socket("localhost",8000);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

        b1 = new JButton("send");
        b1.setBounds(420,125,100,50);
        b1.setBackground(Color.decode("#F0A04B"));

        ExitClient=new JButton("Exit");
        ExitClient.setBounds(80,125,100,50);
        ebHandler = new ExitButtonHandler();
        ExitClient.addActionListener(ebHandler);
        ExitClient.setBackground(Color.decode("#539165"));


        b2 = new JButton("Encrypt");
        b2.setBounds(80,250,100,50);
        b2.setBackground(Color.decode("#A7727D"));
        b2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                l2.setText(finEnc);

            }
        });

        b3 = new JButton("Decryption");
        b3.setBounds(200,250,100,50);
        b3.setBackground(Color.decode("#0E8388"));
        b3.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                l3.setText(finDec);

            }
        });
        ClearCleint=new JButton("Clear All");
        ClearCleint.setBounds(310,250,100,50);
        clbHandler = new ClearButtonHandler();
        ClearCleint.addActionListener( clbHandler);
        ClearCleint.setBackground(Color.decode("#FFDD83"));
        b1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                    String msg = tf.getText();
                    for(int i=0;i<msg.length();i++){
                        int pos = alphabets.indexOf(msg.charAt(i));
                        int enc = (pos + 3)%26;
                        char ind = alphabets.charAt(enc);
                        result += ind;

                    }
                    output.writeUTF(result);
                    String  str2=(String)input.readUTF();
                    finEnc = str2;
                    // l2.setText(finEnc);
                    result = "";
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

                    output.close();
                    socket.close();
                } catch (IOException ex) {
                }

            }
        });

        p1 = new JPanel();
        p1.setBounds(100,330,400,100);
        p1.setBackground(Color.white);
        p1.setLayout(null);
        add(l1);
        add(tf);
        add(b1);
        add(l);
        add(p1);
        add(b2);
        add(b3);
        add(ClearCleint);
        add(ExitClient);
        p1.add(l2);
        p1.add(l3);



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
                try  {
                    new Client();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        });
    }

}
