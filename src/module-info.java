/**
 * 
 */
/**
 * @author alexa
 *
 */
module FarkleAI {
	requires javafx.graphics;
	requires javafx.controls;
	requires javafx.base;
	requires java.desktop;
	
	opens client to javafx.graphics, javafx.base;
}