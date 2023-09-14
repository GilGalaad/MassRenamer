package massrenamer.gui;

public class MassRenamer extends javax.swing.JFrame {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JRadioButton dateAndOrdinalPatternRadioButton;
    protected javax.swing.JButton dirButton;
    protected javax.swing.JLabel dirLabel;
    protected javax.swing.JTextField dirTextField;
    protected javax.swing.JRadioButton ordinalPatternRadioButton;
    protected javax.swing.ButtonGroup patternButtonGroup;
    protected javax.swing.JLabel patternLabel;
    protected javax.swing.JProgressBar progressBar;
    protected javax.swing.ButtonGroup sortButtonGroup;
    protected javax.swing.JRadioButton sortByDateRadioButton;
    protected javax.swing.JRadioButton sortByFilenameRadioButton;
    protected javax.swing.JLabel sortLabel;
    protected javax.swing.JButton startButton;
    protected javax.swing.JLabel tagLabel;
    protected javax.swing.JTextField tagTextField;
    // End of variables declaration//GEN-END:variables

    public MassRenamer() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        patternButtonGroup = new javax.swing.ButtonGroup();
        sortButtonGroup = new javax.swing.ButtonGroup();
        dirLabel = new javax.swing.JLabel();
        dirButton = new javax.swing.JButton();
        dirTextField = new javax.swing.JTextField();
        patternLabel = new javax.swing.JLabel();
        ordinalPatternRadioButton = new javax.swing.JRadioButton();
        dateAndOrdinalPatternRadioButton = new javax.swing.JRadioButton();
        sortLabel = new javax.swing.JLabel();
        sortByFilenameRadioButton = new javax.swing.JRadioButton();
        sortByDateRadioButton = new javax.swing.JRadioButton();
        tagLabel = new javax.swing.JLabel();
        tagTextField = new javax.swing.JTextField();
        startButton = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MassRenamer");
        setResizable(false);

        dirLabel.setText("Directory");
        dirLabel.setFocusable(false);

        dirButton.setText("...");

        dirTextField.setEditable(false);

        patternLabel.setText("Pattern");
        patternLabel.setFocusable(false);

        patternButtonGroup.add(ordinalPatternRadioButton);
        ordinalPatternRadioButton.setSelected(true);
        ordinalPatternRadioButton.setText("Tag and ordinal");
        ordinalPatternRadioButton.setFocusable(false);

        patternButtonGroup.add(dateAndOrdinalPatternRadioButton);
        dateAndOrdinalPatternRadioButton.setText("Tag, date and daily ordinal");
        dateAndOrdinalPatternRadioButton.setFocusable(false);

        sortLabel.setText("Sort by");
        sortLabel.setFocusable(false);

        sortButtonGroup.add(sortByFilenameRadioButton);
        sortByFilenameRadioButton.setSelected(true);
        sortByFilenameRadioButton.setText("Filename");
        sortByFilenameRadioButton.setFocusable(false);

        sortButtonGroup.add(sortByDateRadioButton);
        sortByDateRadioButton.setText("Date (requires Exif)");
        sortByDateRadioButton.setFocusable(false);

        tagLabel.setText("Tag");
        tagLabel.setFocusable(false);

        startButton.setText("Start");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(dirLabel)
                                                        .addComponent(patternLabel)
                                                        .addComponent(tagLabel))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(ordinalPatternRadioButton)
                                                                        .addComponent(sortByFilenameRadioButton))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(sortByDateRadioButton)
                                                                        .addComponent(dateAndOrdinalPatternRadioButton))
                                                                .addGap(0, 252, Short.MAX_VALUE))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(dirTextField)
                                                                        .addComponent(tagTextField))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(startButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(dirButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(sortLabel)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(dirLabel)
                                        .addComponent(dirButton)
                                        .addComponent(dirTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(patternLabel)
                                        .addComponent(ordinalPatternRadioButton)
                                        .addComponent(dateAndOrdinalPatternRadioButton))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(sortLabel)
                                        .addComponent(sortByFilenameRadioButton)
                                        .addComponent(sortByDateRadioButton))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tagTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tagLabel)
                                        .addComponent(startButton))
                                .addGap(18, 18, 18)
                                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

}
