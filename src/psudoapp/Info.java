package psudoapp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Info {

    private String filePath = "HospitalInfo.txt";

    /**
     * Get all the info from a certain key as csv (,,,,)
     *
     * @param Key
     * @return
     */
    public String GetInfoFromKey(String Key) {
        String ans = "";
        String sCurrentLine;
        FileReader fr;
        String newKey = "[" + Key + "]";
        Boolean keyFound = false;
        Boolean dataEndReached = false;

        try {
            fr = new FileReader(getFilePath());
            BufferedReader textReader = new BufferedReader(fr);
            int i = 0;

            while ((sCurrentLine = textReader.readLine()) != null) {
                //Find Key
                if (sCurrentLine.equals(newKey)) {
                    keyFound = true;
                    sCurrentLine = textReader.readLine();
                }
                //Get data
                if (keyFound) {
                    //Check its not a new key
                    if ((sCurrentLine.startsWith("[")) && (sCurrentLine != null)) {
                        dataEndReached = true;
                    } else {
                        ans = ans + sCurrentLine + ",";
                    }
                }
                //Exit if no more data
                if (dataEndReached) {
                    break;
                }
                i++;
            }

            textReader.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ans;
    }

    /**
     * True if Data is found at specific Key
     *
     * @param Key
     * @param Data
     * @return
     */
    public Boolean FindDataFromKey(String Key, String Data) {
        Boolean ans = false;
        String sCurrentLine;
        FileReader fr;
        String newKey = "[" + Key + "]";
        Boolean keyFound = false;
        Boolean dataEndReached = false;

        try {
            fr = new FileReader(getFilePath());
            BufferedReader textReader = new BufferedReader(fr);
            int i = 0;

            while ((sCurrentLine = textReader.readLine()) != null) {
                //Find Key
                if (sCurrentLine.equals(newKey)) {
                    keyFound = true;
                    sCurrentLine = textReader.readLine();
                }

                //Find data
                if (keyFound) {
                    //Check its not a new key
                    if ((sCurrentLine.startsWith("[")) && (sCurrentLine != null)) {
                        dataEndReached = true;
                    } else {
                        if (Data.equals(sCurrentLine)) {
                            ans = true;
                            break;
                        }
                    }
                }
                //Exit if no more data
                if (dataEndReached) {
                    break;
                }
                i++;
            }

            textReader.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ans;
    }

    /**
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @param filePath the filePath to set
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
