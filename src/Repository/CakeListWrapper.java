package Repository;

import Domain.Cake;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "cakes")
public class CakeListWrapper {
    private List<Cake> cakes;

    public CakeListWrapper() {}

    public CakeListWrapper(List<Cake> cakes) {
        this.cakes = cakes;
    }

    @XmlElement(name = "cake")
    public List<Cake> getCakes() {
        return cakes;
    }

    public void setCakes(List<Cake> cakes) {
        this.cakes = cakes;
    }
}
