package Repository;

import Domain.Cake;

import javax.xml.bind.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class CakesXMLFile implements IRepository<Integer, Cake> {
    private final String filePath;
    private List<Cake> cakes = new ArrayList<>();

    public CakesXMLFile(String filePath) {
        this.filePath = filePath;
        loadCakes();
    }

    private void loadCakes() {
        try {
            File file = new File(filePath);
            if (file.exists() && file.length() > 0) {  // Check if file is not empty
                JAXBContext context = JAXBContext.newInstance(CakeListWrapper.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                CakeListWrapper wrapper = (CakeListWrapper) unmarshaller.unmarshal(file);
                cakes = wrapper.getCakes();
            }
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to load cakes from XML file: " + e.getMessage());
        }
    }

    private void saveCakes() {
        try {
            JAXBContext context = JAXBContext.newInstance(CakeListWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(new CakeListWrapper(cakes), new File(filePath));
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to save cakes to XML file: " + e.getMessage());
        }
    }

    @Override
    public void addEntity(Integer id, Cake cake) {
        cakes.add(cake);
        saveCakes();
    }

    @Override
    public void updateEntity(Integer id, Cake cake) {
        cakes.replaceAll(c -> c.getId() == id ? cake : c);
        saveCakes();
    }

    @Override
    public Optional<Cake> deleteEntity(Integer id) {
        if(cakes.contains(id)) {
            Cake cake = cakes.get(id);
            cakes.remove(id);
            saveCakes();
            return Optional.of(cake);
        }
        return null;
    }

    @Override
    public Optional<Cake> findEntity(Integer id) {
        Cake cake = cakes.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
        return Optional.ofNullable(cake);
    }

    @Override
    public Iterator<Cake> findAllEntities() {
        return cakes.iterator();
    }
}
