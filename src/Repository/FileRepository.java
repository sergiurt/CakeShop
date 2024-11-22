package Repository;

import Domain.Identifiable;
import Exceptions.FileNotValidException;

import java.util.Optional;

public abstract class FileRepository<ID, T extends Identifiable> extends Repository<ID, T>{
    protected String filename;

    // Protected no-arg constructor for delayed initialization
    protected FileRepository() {
        super(); // Initialize the base Repository
    }

    protected FileRepository(String filename)throws FileNotValidException{
        this();
        this.filename = filename;
        readFile();
    }

    protected abstract void readFile() throws FileNotValidException;
    protected abstract void writeFile();

    @Override
    public void addEntity(ID id, T entity){
        super.addEntity(id, entity);
        writeFile();
    }

    @Override
    public Optional<T> deleteEntity(ID id){
        if(super.findEntity(id).isPresent()){
            T entity = super.findEntity(id).get();
            super.deleteEntity(id);
            writeFile();
            return Optional.of(entity);
        }
        return null;
    }

    @Override
    public void updateEntity(ID id, T t) {
        super.updateEntity(id, t);
        writeFile();
    }
}