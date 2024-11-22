package Repository;

import Domain.Cake;
import Exceptions.FileNotValidException;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class CakesShopTextFile extends FileRepository<Integer,Cake>{

    public CakesShopTextFile(String fileName)throws FileNotValidException {
        super(fileName);
    }

    @Override
    public void readFile() throws FileNotValidException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename)))
        {
            String line;
            while((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if(tokens.length != 6){
                    throw new FileNotValidException("File not valid!");
                }
                else{
                    Cake cake = new Cake(tokens[0], Integer.parseInt(tokens[1]), tokens[2], tokens[3],  Integer.parseInt(tokens[4]),  Integer.parseInt(tokens[5]));
                    map.put(cake.getId(), cake);
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void writeFile(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {

            Iterator<Cake> it = findAllEntities();
            while(it.hasNext()) {
                Cake cake = it.next();
                bw.write(cake.getName()+","+cake.getPrice()+","+cake.getDescription()+","+cake.getSize()+","+cake.getQuantity()+","+cake.getId()+"\n");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
