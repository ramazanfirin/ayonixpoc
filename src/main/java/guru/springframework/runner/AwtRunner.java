package guru.springframework.runner;

import guru.springframework.swing.components.WebcamViewerExample2;

import java.awt.Toolkit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Launches the application GUI.
 *
 * @author Paul Campbell
 */
@Component
public class AwtRunner implements CommandLineRunner {

    /**
     * Pull in the JFrame to be displayed.
     */
    @Autowired(required=true)
    private WebcamViewerExample2 frame;

    @Override
    public void run(String... args) throws Exception {
        /* display the form using the AWT EventQueue */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
            	Toolkit tk = Toolkit.getDefaultToolkit();
        		int xSize = ((int) tk.getScreenSize().getWidth());
        		int ySize = ((int) tk.getScreenSize().getHeight());
        		frame.setSize(xSize,ySize);
        		System.out.println("frame size "+ xSize +" "+ ySize);
            	frame.setVisible(true);
            }
        });
    }

}