@FunctionalInterface
public interface WithdrawRule {
    boolean canWithdraw(long amount);
}
