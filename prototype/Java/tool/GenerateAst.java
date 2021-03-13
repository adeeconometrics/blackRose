package prototype.Java.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * tiny commandline app that generates file named Expr.java; 
 * this class automates how that file gets authored
 */
public class GenerateAst {
    public static void main(String [] args) throws IOException{
        if(args.length != 1){
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];

        defineAst(outputDir, "Expr", Arrays.asList(
            "Binary   : Expr left, Token operator, Expr right",
            "Grouping : Expr expression",
            "Literal  : Object value",
            "Unary    : Token operator, Expr right"
        ));
    }
    /**
     * outputs the base Expr class.
     */
    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException{
        String path = outputDir+"/"+baseName+".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package prototype.Java");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class "+baseName+" {");
        // visitors
        defineVisitor(writer, baseName, types);

        // base accept method
        writer.println();
        writer.println("    abstract <R> R accept(Visitor <R> visitor);");

        writer.println("}");
        writer.close();

        //the AST classes
        for(String type: types){
            String className = type.split(":")[0].trim(0);
            String fields = type.split(":")[1].trim(0);     
            defineType(writer, baseName, className, fields);   
        }
        
    }

    private static void defineType(PrintWriter writer, String baseName, String className, String fieldList){
        writer.println(" static class "+ className + " extends "+ baseName + "{");
        // constructor
        writer.println("    " + className + "(" + fieldList + "){");
        // store parameters in fields
        String[] fields = fieldList.split(",");
        for(String field: fields){
            String name = field.split(" ")[1];
            writer.print("  this."+ name + " = "+ name + ";");
        }

        writer.println("    }");

        // visitor pattern
        writer.println();
        writer.println("    @Override");
        writer.println("    <R> R accept(Visitor <R> visitor) {");
        writer.println("        return visitor.visit" + className + baseName + "(this);");
        writer.println("    }");

        // fields
        writer.println();
        for(String field: fields){
            writer.println("    final " + field + ";");
        }
        
        writer.println("}");
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types){
        writer.print("  interface Visitor<R> {");

        for(String type: types){
            String typeName = type.split(",")[0].trim();
            writer.println("    R visit" + typeName + baseName + "(" + typeName + " " + baseName.toLowerCase() + ");");
        }

        writer.println();
    }
}
