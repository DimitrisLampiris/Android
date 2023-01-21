
package Model;

public class Data {
    String title ;
    String price ;
    String image ;
    String amount;
    String id ;

    public Data()
    {

    }

    public Data(String title, String price,String amount, String image, String id) {
        this.title = title;
        this.price = price;
        this.image = image;
        this.amount=amount;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getAmount() {
        return amount;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setId(String id) {
        this.id = id;
    }
}