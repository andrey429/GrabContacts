import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Andrey
 * Date: 03.04.13
 * Time: 16:28
 * To change this template use File | Settings | File Templates.
 */
public class GrabContacts {


    public static String decode(String encodedQuoted) {

        byte[] byteArray = encodedQuoted.getBytes();
        byte escape = '=';

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            for (int i = 0; i < byteArray.length; i++) {
                int b = byteArray[i];
                if (b == escape) {
                    int u = Character.digit(byteArray[++i], 16);
                    int l = Character.digit(byteArray[++i], 16);
                    buffer.write((u << 4) | l);
                } else {
                    buffer.write(b);
                }
            }
        /*this hack if for shitty format like the following:
            ..N;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:=A0=BC=CD=\r\n
                    =AB=FE=...
                    it will generate index out of bounds exception because of the lonely '=' sign after 0xCD hex value

        *
        *
        * */
        } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsExc) {
            new String(buffer.toByteArray(), Charset.forName("UTF-8"));
        }
        return new String(buffer.toByteArray(), Charset.forName("UTF-8"));


    }


    public static void main(String args[]) {


        if (args.length != 2) {
            System.out.println("Usage: java GrabContacts [input .vcf file] [output .txt file]\r\n" +
                    "Note: output file columns are '#' separated, rows are \\r\\n separated\r\n");
            return;
        }
        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);
        if (!inputFile.exists()) {
            System.out.println("Error: no such input file exists");
            return;
        }

        List<VCard> vCards = new ArrayList<VCard>();
        try {

            BufferedReader rdr = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
            String temp, val;


            while ((temp = rdr.readLine()) != null) {
                VCard vCard = new VCard();
                while (!(temp = rdr.readLine()).contains("END")) {
                    if (temp.contains("N;") && !temp.contains("FN;")) {
                        val = temp.substring(temp.indexOf(":") + 1);
                        vCard.setName(decode(val));
                    }
                    if (temp.contains("TEL")) {
                        val = temp.substring(temp.indexOf(":") + 1);
                        vCard.addPhone(val);
                    }

                }
                if (vCard.getName() != null || !vCard.getPhones().isEmpty()) {
                    vCards.add(vCard);


                }


            }


            rdr.close();

        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            BufferedWriter wrt = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
            for (VCard vCard : vCards) {
                wrt.write(vCard.getName() + "#");
                for (String phone : vCard.getPhones()) {
                    wrt.write(phone + "#");
                }
                wrt.write("\r\n");
                wrt.flush();
            }
            wrt.close();
            System.out.println("Finish : " + vCards.size() + " contacts written to " + args[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
