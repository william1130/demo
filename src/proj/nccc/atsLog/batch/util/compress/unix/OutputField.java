package proj.nccc.atsLog.batch.util.compress.unix;

import java.awt.*;

public class OutputField extends TextField {

    OutputField() {
        this.setEditable(false);
    }

    void addString(String str) {
        int last;

        this.setText(this.getText() + str);
        last = this.getText().length();
        this.select(last, last);
    }
}
