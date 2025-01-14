/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tags;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author manhc
 */
public class custom extends SimpleTagSupport {

    private String textColor;

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    /**
     * Called by the container to invoke this tag. The implementation of this
     * method is provided by the tag library developer, and handles all tag
     * processing, body iteration, etc.
     */
    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();

        try {
            out.println("<style>");
            out.println("#TEXT { color: " + textColor + "; }");
            out.println("</style>");
            out.println("<button onclick=\"changeColor()\">Change color</button>");
            out.println("<p id=\"TEXT\">Text</p>");
            out.println("<script>");
            out.println("function changeColor() {");
            out.println("  var text = document.getElementById('TEXT');");
            out.println("  if (text.style.color == '" + textColor + "') {");
            out.println("    text.style.color = 'black';");
            out.println("  } else {");
            out.println("    text.style.color = '" + textColor + "';");
            out.println("  }");
            out.println("}");
            out.println("</script>");
            // TODO: insert code to write html before writing the body content.
            // e.g.:
            //
            // out.println("<strong>" + attribute_1 + "</strong>");
            // out.println("    <blockquote>");

            JspFragment f = getJspBody();
            if (f != null) {
                f.invoke(out);
            }

            // TODO: insert code to write html after writing the body content.
            // e.g.:
            //
            // out.println("    </blockquote>");
        } catch (java.io.IOException ex) {
            throw new JspException("Error in custom tag", ex);
        }
    }

}
