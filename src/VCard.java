import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Andrey
 * Date: 03.04.13
 * Time: 16:36
 * To change this template use File | Settings | File Templates.
 */
public class VCard {

    private String version;
    private String name;
    private String fullName;
    private List<String> phones;

    public VCard() {
        phones = new ArrayList<String>();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void addPhone(String phone) {
        phones.add(phone);
    }

    @Override
    public String toString() {
        return "VCard{" +
                "name='" + name + '\'' +
                ", phones=" + phones +
                '}';
    }



}
