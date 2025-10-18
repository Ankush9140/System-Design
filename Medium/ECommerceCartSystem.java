import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


    class Product{
        private final String id;
        private final String name;
        private final double price;
        public Product(String id,String name,double price){
            this.id = id;
            this.name = name;
            this.price = price;
        }
        public String getId(){return id;}
        public String getName(){return name;}
        public double getPrice(){return price;}
    }
    class CartItem{
        private Product product;
        private int quantity;
        public CartItem(Product product,int quantity){
            this.product = product;
            this.quantity = quantity;
        }
        public void setQuantity(int quantity){ this.quantity = quantity; }
        public int getQuantity(){ return quantity; }
        public double getPrice(){ return (product.getPrice()*this.quantity); }
        public Product getProduct() { return product; }
    }
    class Cart{
        private HashMap<String,CartItem>items = new HashMap<>();
        public void addItem(Product product, int quantity) {
            if (items.containsKey(product.getId())) {
                CartItem cartItem = items.get(product.getId());
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            } else {
                items.put(product.getId(), new CartItem(product, quantity));
            }
        }
        public void updateQuantity(String productId,int quantity){
            if(items.containsKey(productId)){
                if(quantity<=0)items.remove(productId);
                else items.get(productId).setQuantity(quantity);
            }
        }
        public void removeItem(String productId){
            if(items.containsKey(productId)){
                items.remove(productId);
            }
        }
        public double getTotalAmount(){
            double amount = 0;
            for(String productId  : items.keySet()){
                amount += items.get(productId).getPrice();
            }
            return amount;
        }
        public List<CartItem> getItems(){
            return new ArrayList<>(items.values());
        }
        public void clearCart(){
            items.clear();
        }
    }
    class User{
        private final String name;
        private final String userId;
        private Cart cart;
        public User(String name,String userId){
            this.name = name;
            this.userId = userId;
            this.cart = new Cart();
        }
        public String getId(){return userId;}
        public String getName(){return name;}
        public Cart getCart(){return cart;}
    }
    class Order{
        private final String orderId;
        private List<CartItem> orderItems;
        private double orderAmount;
        public Order(String orderId,List<CartItem> orderItems,double orderAmount){
            this.orderItems = new ArrayList<>(orderItems);
            this.orderId = orderId;
            this.orderAmount = orderAmount;
        }
        public String getOrderId() { return orderId; }
        public List<CartItem> getItems() { return orderItems; }
        public double getTotalAmount() { return orderAmount; }
    }
    class CartService {
        public void displayCart(User user) {
            Cart cart = user.getCart();
            List<CartItem> items = cart.getItems();
            if (items.isEmpty()) {
                System.out.println("Cart is empty.");
                return;
            }
            System.out.println("\nCart Details:");
            for (CartItem item : items) {
                System.out.println(item.getProduct().getName() + " x " + item.getQuantity() +
                        " = $" + String.format("%.2f", item.getPrice()));
            }
            System.out.println("Total Cart Amount: $" + String.format("%.2f", cart.getTotalAmount()));
        }

        public void checkout(User user){
            Cart cart = user.getCart();
            List<CartItem> cartItems = cart.getItems();
            if (cartItems.isEmpty()) {
                System.out.println("Cart is empty.");
                return;
            }
            String orderId = UUID.randomUUID().toString();
            Order order = new Order(orderId, cartItems, cart.getTotalAmount());
            System.out.println("\n✅ Order placed successfully with ID: " + order.getOrderId());
            displayCart(user);
            cart.clearCart();
        }
    }

   public class ECommerceCartSystem {
    public static void main(String[] args) {
        User user = new User("Ankush", "U00001");
        System.out.println("Welcome, " + user.getName() + "!");

        Product phone = new Product("P001", "Smartphone", 799.99);
        Product laptop = new Product("P002", "Laptop", 1299.49);
        Product headphones = new Product("P003", "Wireless Headphones", 199.99);
        Product tablet = new Product("P004", "Tablet", 499.99);

        Cart cart = user.getCart();
        cart.addItem(phone, 1);
        cart.addItem(laptop, 1);
        cart.addItem(headphones, 2);
        cart.addItem(tablet, 1);

        System.out.println("\nItems added to cart.");

        cart.updateQuantity("P003", 3);
        cart.removeItem("P004");

        CartService cartService = new CartService();
        cartService.displayCart(user);

        System.out.println("\nProceeding to checkout...");
        cartService.checkout(user);
    }
}

