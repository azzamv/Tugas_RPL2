package perpustakaan.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import perpustakaan.App;
import perpustakaan.Muat;
import perpustakaan.Tema;
import perpustakaan.model.Buku;
import perpustakaan.service.BukuService;

public class BukuPanel extends JPanel implements Muat {
    private final BukuService svc = App.get(BukuService.class);
    private final JTextField fJudul = new JTextField();
    private final JTextField fPengarang = new JTextField();
    private final JTextField fPenerbit = new JTextField();
    private final JTextField fTahun = new JTextField();
    private final JTextField fStok = new JTextField();
    private final JTable tabel = new JTable();
    private Integer id = null;
    private final JButton bTambah = Tema.tombol("Tambah");
    private final JButton bUbah = Tema.tombol("Ubah");
    private final JButton bHapus = Tema.tombolNetral("Hapus");
    private final JButton bClear = Tema.tombolNetral("Clear");

    public BukuPanel() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Tema.LATAR);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));
        add(form(), BorderLayout.NORTH);
        add(tabelBox(), BorderLayout.CENTER);

        Tema.styleField(fJudul); Tema.styleField(fPengarang); Tema.styleField(fPenerbit);
        Tema.styleField(fTahun); Tema.styleField(fStok);
        Tema.styleTabel(tabel);

        tabel.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int r = tabel.getSelectedRow(); if (r < 0) return;
            id = (Integer) tabel.getValueAt(r, 0);
            fJudul.setText(v(r,1)); fPengarang.setText(v(r,2)); fPenerbit.setText(v(r,3));
            fTahun.setText(v(r,4)); fStok.setText(v(r,5));
        });
        bTambah.addActionListener(e -> simpan(false));
        bUbah.addActionListener(e -> simpan(true));
        bHapus.addActionListener(e -> hapus());
        bClear.addActionListener(e -> clear());
    }

    private JPanel form() {
        Tema.Kartu k = new Tema.Kartu(Tema.PUTIH);
        k.setLayout(new GridBagLayout());
        k.setBorder(BorderFactory.createCompoundBorder(Tema.garis(), BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6); c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;

        addField(k, c, 0, 0, "Judul", fJudul); addField(k, c, 1, 0, "Pengarang", fPengarang);
        addField(k, c, 0, 1, "Penerbit", fPenerbit); addField(k, c, 1, 1, "Tahun", fTahun);
        addField(k, c, 0, 2, "Stok", fStok);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btns.setOpaque(false);
        btns.add(bTambah); btns.add(bUbah); btns.add(bHapus); btns.add(bClear);
        c.gridx = 1; c.gridy = 2; c.anchor = GridBagConstraints.SOUTHWEST; c.fill = GridBagConstraints.NONE; c.weightx = 0;
        k.add(btns, c);
        return k;
    }

    private void addField(JPanel p, GridBagConstraints c, int x, int y, String label, JTextField f) {
        JPanel w = new JPanel(new BorderLayout(0, 4)); w.setOpaque(false);
        JLabel l = new JLabel(label); l.setFont(Tema.FONT_TEBAL); l.setForeground(Tema.TEKS);
        w.add(l, BorderLayout.NORTH); w.add(f, BorderLayout.CENTER);
        GridBagConstraints gc = (GridBagConstraints) c.clone();
        gc.gridx = x; gc.gridy = y; gc.weightx = 1; gc.fill = GridBagConstraints.HORIZONTAL;
        p.add(w, gc);
    }

    private JComponent tabelBox() {
        Tema.Kartu k = new Tema.Kartu(Tema.PUTIH);
        k.setLayout(new BorderLayout());
        k.setBorder(BorderFactory.createCompoundBorder(Tema.garis(), BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        JScrollPane sp = new JScrollPane(tabel);
        sp.setBorder(Tema.garis());
        sp.getViewport().setBackground(Tema.PUTIH);
        k.add(sp, BorderLayout.CENTER);
        return k;
    }

    private String v(int r, int c) { Object o = tabel.getValueAt(r, c); return o == null ? "" : o.toString(); }

    private void setBusy(boolean b) {
        bTambah.setEnabled(!b); bUbah.setEnabled(!b); bHapus.setEnabled(!b); bClear.setEnabled(!b);
        setCursor(b ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor());
    }

    private void simpan(boolean ubah) {
        if (ubah && id == null) { JOptionPane.showMessageDialog(this, "Pilih data dulu"); return; }
        final Integer fid = id;
        String j=fJudul.getText(), p=fPengarang.getText(), pb=fPenerbit.getText(), t=fTahun.getText(), s=fStok.getText();
        setBusy(true);
        new SwingWorker<Void, Void>() {
            Exception err;
            protected Void doInBackground() {
                try { if (ubah) svc.ubah(fid, j,p,pb,t,s); else svc.simpan(j,p,pb,t,s); } catch (Exception e) { err = e; }
                return null;
            }
            protected void done() { setBusy(false); if (err!=null) err(err); else { clear(); muat(); } }
        }.execute();
    }

    private void hapus() {
        if (id == null) { JOptionPane.showMessageDialog(this, "Pilih data dulu"); return; }
        if (JOptionPane.showConfirmDialog(this, "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        final Integer fid = id;
        setBusy(true);
        new SwingWorker<Void, Void>() {
            Exception err;
            protected Void doInBackground() { try { svc.hapus(fid); } catch (Exception e) { err = e; } return null; }
            protected void done() { setBusy(false); if (err!=null) err(err); else { clear(); muat(); } }
        }.execute();
    }

    private void clear() { id=null; fJudul.setText(""); fPengarang.setText(""); fPenerbit.setText(""); fTahun.setText(""); fStok.setText(""); tabel.clearSelection(); }
    private void err(Exception e) { e.printStackTrace(); JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }

    @Override
    public void muat() {
        setBusy(true);
        new SwingWorker<List<Buku>, Void>() {
            Exception err;
            protected List<Buku> doInBackground() { try { return svc.semua(); } catch (Exception e) { err = e; return List.of(); } }
            protected void done() {
                setBusy(false);
                if (err != null) { err(err); return; }
                try {
                    List<Buku> list = get();
                    String[] kol = {"ID","Judul","Pengarang","Penerbit","Tahun","Stok"};
                    Object[][] d = new Object[list.size()][6];
                    for (int i=0;i<list.size();i++){ Buku b=list.get(i); d[i]=new Object[]{b.getId(),b.getJudul(),b.getPengarang(),b.getPenerbit(),b.getTahun(),b.getStok()}; }
                    tabel.setModel(new DefaultTableModel(d, kol){ public boolean isCellEditable(int r,int c){return false;} });
                    if (tabel.getColumnModel().getColumnCount()>0) tabel.getColumnModel().getColumn(0).setMaxWidth(60);
                } catch (Exception e) { err(e); }
            }
        }.execute();
    }
}
