package prototype.Java;

// base class for all expresson classes inherit from
abstract class Expr{
    static class Binary extends Expr{
        Binary(Expr left, Token operator, Expr right){
            this.left = left;
            this.operator = operator;
            this.right = right;
        }
        final Expr left;
        final Token operator;
        final Expr right;
    }

    // static class Unary extends Expr{
    //     Unary(Token oeprator, Expr left){
    //         this.operator = operator;
    //         this.left = left;
    //     }
    //     final Token operator;
    //     final Expr left;
    // }
}