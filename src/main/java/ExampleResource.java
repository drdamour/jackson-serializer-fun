public class ExampleResource implements Resource {
    private Link a = new Link("link a");
    public Link getA(){
        return a;
    }

    private Link b = new Link("link b");
    public Link getB(){
        return b;
    }

    private Thing c = new Thing("other c");
    public Thing getC(){
        return c;
    }

    private Link[] d = new Link[]{new Link("link d1"), new Link("link d2")};
    public Link[] getD(){return d;}
}
