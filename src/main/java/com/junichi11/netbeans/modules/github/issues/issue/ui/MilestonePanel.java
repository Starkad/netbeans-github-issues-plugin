/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2015 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2015 Sun Microsystems, Inc.
 */
package com.junichi11.netbeans.modules.github.issues.issue.ui;

import com.junichi11.netbeans.modules.github.issues.GitHubIcons;
import java.awt.Dialog;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.eclipse.egit.github.core.Milestone;
import org.netbeans.api.annotations.common.CheckForNull;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;

/**
 *
 * @author junichi11
 */
public class MilestonePanel extends javax.swing.JPanel {

    private static final long serialVersionUID = -4759519190789532723L;
    private final ChangeSupport changeSupport = new ChangeSupport(this);
    private String errorMessage;
    private final List<Milestone> filter;

    /**
     * Creates new form MilestonePanel
     */
    public MilestonePanel(List<Milestone> filter) {
        if (filter == null) {
            this.filter = Collections.emptyList();
        } else {
            this.filter = filter;
        }
        initComponents();
        // add listener
        DocumentListenerImpl documentListener = new DocumentListenerImpl();
        titleTextField.getDocument().addDocumentListener(documentListener);
        dueDateDatePicker.setFormats(new SimpleDateFormat("yyyy/MM/dd")); // NOI18N
        errorLabel.setForeground(UIManager.getColor("nb.errorForeground")); // NOI18N
        setError(""); // NOI18N
    }

    @NbBundle.Messages({
        "MilestonePanel.title=New milestone"
    })
    @CheckForNull
    public static Milestone showDialog(List<Milestone> filter) {
        final MilestonePanel panel = new MilestonePanel(filter);
        final DialogDescriptor descriptor = new DialogDescriptor(panel, Bundle.MilestonePanel_title(), true, DialogDescriptor.OK_CANCEL_OPTION, null, null);
        ChangeListener changeListener = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                descriptor.setValid(panel.getErrorMessage() == null);
                panel.setError(panel.getErrorMessage());
            }
        };
        panel.addChangeListener(changeListener);
        panel.fireChange();
        Dialog dialog = DialogDisplayer.getDefault().createDialog(descriptor);
        dialog.pack();
        dialog.setVisible(true);
        dialog.dispose();
        panel.removeChangeListener(changeListener);
        if (descriptor.getValue() == DialogDescriptor.OK_OPTION) {
            Milestone milestone = new Milestone()
                    .setTitle(panel.getTitle())
                    .setState("open"); // NOI18N

            String description = panel.getDescription();
            if (!description.isEmpty()) {
                milestone = milestone.setDescription(description);
            }

            Date dueDate = panel.getDueDate();
            if (dueDate != null) {
                milestone = milestone.setDueOn(dueDate);
            }

            return milestone;
        }
        return null;
    }

    public String getTitle() {
        return titleTextField.getText().trim();
    }

    public String getDescription() {
        return descriptionTextArea.getText();
    }

    public Date getDueDate() {
        return dueDateDatePicker.getDate();
    }

    public final void setError(String errorMessage) {
        if (errorMessage == null || errorMessage.isEmpty()) {
            errorLabel.setText(""); // NOI18N
            errorLabel.setIcon(null);
            return;
        }
        errorLabel.setText(errorMessage);
        errorLabel.setIcon(GitHubIcons.ERROR_ICON_16);
    }

    void fireChange() {
        validateInputValues();
        changeSupport.fireChange();
    }

    void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    @NbBundle.Messages({
        "MilestonePanel.validate.title.empty=Title must be set.",
        "# {0} - title",
        "MilestonePanel.validate.name.alreadyExists={0} already exists."
    })
    private void validateInputValues() {
        String title = getTitle();
        if (title.isEmpty()) {
            errorMessage = Bundle.MilestonePanel_validate_title_empty();
            return;
        }

        // already exist?
        for (Milestone milestone : filter) {
            if (milestone == null) {
                continue;
            }
            if (milestone.getTitle().equals(title)) {
                errorMessage = Bundle.MilestonePanel_validate_name_alreadyExists(title);
                return;
            }
        }
        errorMessage = null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        titleTextField = new javax.swing.JTextField();
        descriptionLabel = new javax.swing.JLabel();
        dueDateDatePicker = new org.jdesktop.swingx.JXDatePicker();
        dueDateLabel = new javax.swing.JLabel();
        errorLabel = new javax.swing.JLabel();
        descriptionScrollPane = new javax.swing.JScrollPane();
        descriptionTextArea = new javax.swing.JTextArea();

        org.openide.awt.Mnemonics.setLocalizedText(titleLabel, org.openide.util.NbBundle.getMessage(MilestonePanel.class, "MilestonePanel.titleLabel.text")); // NOI18N

        titleTextField.setText(org.openide.util.NbBundle.getMessage(MilestonePanel.class, "MilestonePanel.titleTextField.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(descriptionLabel, org.openide.util.NbBundle.getMessage(MilestonePanel.class, "MilestonePanel.descriptionLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(dueDateLabel, org.openide.util.NbBundle.getMessage(MilestonePanel.class, "MilestonePanel.dueDateLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(errorLabel, org.openide.util.NbBundle.getMessage(MilestonePanel.class, "MilestonePanel.errorLabel.text")); // NOI18N

        descriptionTextArea.setColumns(20);
        descriptionTextArea.setRows(5);
        descriptionScrollPane.setViewportView(descriptionTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descriptionScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(titleTextField)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(descriptionLabel)
                                    .addComponent(titleLabel))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dueDateLabel)
                            .addComponent(dueDateDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(errorLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleLabel)
                    .addComponent(dueDateLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dueDateDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(descriptionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descriptionScrollPane)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(errorLabel)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JScrollPane descriptionScrollPane;
    private javax.swing.JTextArea descriptionTextArea;
    private org.jdesktop.swingx.JXDatePicker dueDateDatePicker;
    private javax.swing.JLabel dueDateLabel;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JTextField titleTextField;
    // End of variables declaration//GEN-END:variables

    private class DocumentListenerImpl implements DocumentListener {

        public DocumentListenerImpl() {
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            processUpdate();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            processUpdate();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            processUpdate();
        }

        private void processUpdate() {
            fireChange();
        }
    }

}
