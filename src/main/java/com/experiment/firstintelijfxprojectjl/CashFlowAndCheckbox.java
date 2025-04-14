/**
 * CashFlowAndCheckbox.java         Eclipse IDE

 * @author Justin Larsen
 * 8/3/2024
 *
 * Description:  Class that bundles a Cash Flow with a checkbox. Used to keep track of which cash flow is slected
 * by checkbox.
 */

package com.experiment.firstintelijfxprojectjl;

import javafx.scene.control.CheckBox;

public class CashFlowAndCheckbox {
    private CashFlow cashFlow;
    private CheckBox checkBox;

    public CashFlowAndCheckbox(CashFlow cashFlow, int fontSize)
    {
        this.cashFlow = cashFlow;
        checkBox = new CheckBox();
        checkBox.setStyle(
                "-fx-pref-width: 15px; " + // Increase the width of the checkbox
                        "-fx-pref-height: 15px; " + // Increase the height of the checkbox
                        "-fx-font-size: "+ fontSize +"px; " + // Increase the font size for the label
                        "-fx-scale-x: 1; " + // Scale the width of the check mark
                        "-fx-scale-y: 1;" // Scale the height of the check mark
        );
    }

    public CashFlow getCashFlow()
    {
        return cashFlow;
    }

    public CheckBox getCheckBox()
    {
        return checkBox;
    }
}
