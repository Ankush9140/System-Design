public class RateLimiter {
    private final int capacity;
    private final double refillRatePerSecond;
    private double availableTokens;
    private long lastRefillTimestamp;
    public RateLimiter(int capacity,double refillRatePerSecond){
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
        this.availableTokens = capacity;
        this.lastRefillTimestamp = System.nanoTime();
    }
    public synchronized boolean allowRequest(){
        refill();
        if(availableTokens>=1){
            availableTokens--;
            return true;
        }
        return false;
    }
    private void refill(){
        long now = System.nanoTime();
        double tokenToAdd = ((now-lastRefillTimestamp)/1e9) * refillRatePerSecond;
        availableTokens = Math.min(capacity,availableTokens+tokenToAdd);
        lastRefillTimestamp = now;
    }
    public static void main(String[] args) throws InterruptedException {
        RateLimiter rateLimiter = new RateLimiter(5,1);
        for(int i=0;i<10;i++){
            if(rateLimiter.allowRequest()){
                System.out.println("Request allowed at " + System.currentTimeMillis());
            }
            else{
                System.out.println("Request denied (Rate limit hit) at " + System.currentTimeMillis());
            }
            Thread.sleep(300);
        }
    }
}
