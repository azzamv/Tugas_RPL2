package perpustakaan.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import perpustakaan.App;
import perpustakaan.Muat;
import perpustakaan.Tema;
import perpustakaan.model.Anggota;
import perpustakaan.service.AnggotaService;

public class AnggotaPanel extends JPanel implements Muat {
    private final AnggotaService svc = App.get(AnggotaService.class);
    private final JTextField fNama = new JTextField();
    private final JTextField fAlamat = new JTextField();
    private final JTextField fTelp = new JTextField();
    private final JComboBox<String> fStatus = new JComboBox<>(new String[]{"Aktif", "Nonaktif"});
    private final JTable tabel = new JTable();
    private Integer id = null;
    private final JButton bTambah = Tema.tombol("Tambah");
    private final JButton bUbah = Tema.tombol("Ubah");
    private final JButton bHapus = Tema.tombolNetral("Hapus");
    private final JButton bClear = Tema.tombolNetral("Clear");

    public AnggotaPanel() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Tema.LATAR);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));
        add(form(), BorderLayout.NORTH);
        add(tabelBox(), BorderLayout.CENTER);

        Tema.styleField(fNama); Tema.styleField(fAlamat); Tema.styleField(fTelp);
        fStatus.setFont(Tema.FONT_BIASA); fStatus.setBackground(Tema.PUTIH);
        Tema.styleTabel(tabel);

        tabel.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int r = tabel.getSelectedRow(); if (r < 0) return;
            id = (Integer) tabel.getValueAt(r, 0);
            fNama.setText(v(r,1)); fAlamat.setText(v(r,2)); fTelp.setText(v(r,3)); fStatus.setSelectedItem(v(r,4));
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
        addField(k, c, 0, 0, "Nama", fNama); addField(k, c, 1, 0, "Alamat", fAlamat);
        addField(k, c, 0, 1, "Telepon", fTelp);
        addField(k, c, 1, 1, "Status", fStatus);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btns.setOpaque(false);
        btns.add(bTambah); btns.add(bUbah); btns.add(bHapus); btns.add(bClear);
        c.gridx = 0; c.gridy = 2; c.gridwidth = 2; c.weightx = 0; c.insets = new Insets(10, 6, 4, 6);
        k.add(btns, c);
        return k;
    }

    private void addField(JPanel p, GridBagConstraints c, int x, int y, String label, JComponent f) {
        JPanel w = new JPanel(new BorderLayout(0, 4)); w.setOpaque(false);
        JLabel l = new JLabel(label); l.setFont(Tema.FONT_TEBAL); l.setForeground(Tema.TEKS);
        w.add(l, BorderLayout.NORTH); w.add(f, BorderLayout.CENTER);
        GridBagConstraints gc = (GridBagConstraints) c.clone();
        gc.gridx = x; gc.gridy = y; gc.gridwidth = 1; gc.weightx = 1; gc.fill = GridBagConstraints.HORIZONTAL;
        p.add(w, gc);
    }

    private JComponent tabelBox() {
        Tema.Kartu k = new Tema.Kartu(Tema.PUTIH);
        k.setLayout(new BorderLayout());
        k.setBorder(BorderFactory.createCompoundBorder(Tema.garis(), BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        JScrollPane sp = new JScrollPane(tabel); sp.setBorder(Tema.garis()); sp.getViewport().setBackground(Tema.PUTIH);
        k.add(sp, BorderLayout.CENTER); return k;
    }

    private String v(int r, int c) { Object o = tabel.getValueAt(r, c); return o == null ? "" : o.toString(); }
    private void setBusy(boolean b) { bTambah.setEnabled(!b); bUbah.setEnabled(!b); bHapus.setEnabled(!b); bClear.setEnabled(!b); setCursor(b?Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR):Cursor.getDefaultCursor()); }

    private void simpan(boolean ubah) {
        if (ubah && id == null) { JOptionPane.showMessageDialog(this, "Pilih data dulu"); return; }
        final Integer fid = id; String n=fNama.getText(), a=fAlamat.getText(), t=fTelp.getText(), s=(String)fStatus.getSelectedItem();
        setBusy(true);
        new SwingWorker<Void, Void>() {
            Exception err;
            protected Void doInBackground() { try { if (ubah) svc.ubah(fid,n,a,t,s); else svc.simpan(n,a,t,s); } catch (Exception e) { err = e; } return null; }
            protected void done() { setBusy(false); if (err!=null) err(err); else { clear(); muat(); } }
        }.execute();
    }

    private void hapus() {
        if (id == null) { JOptionPane.showMessageDialog(this, "Pilih data dulu"); return; }
        if (JOptionPane.showConfirmDialog(this, "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        final Integer fid = id; setBusy(true);
        new SwingWorker<Void, Void>() {
            Exception err; protected Void doInBackground() { try { svc.hapus(fid); } catch (Exception e) { err = e; } return null; }
            protected void done() { setBusy(false); if (err!=null) err(err); else { clear(); muat(); } }
        }.execute();
    }

    private void clear() { id=null; fNama.setText(""); fAlamat.setText(""); fTelp.setText(""); fStatus.setSelectedIndex(0); tabel.clearSelection(); }
    private void err(Exception e) { e.printStackTrace(); JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }

    @Override
    public void muat() {
        setBusy(true);
        new SwingWorker<List<Anggota>, Void>() {
            Exception err;
            protected List<Anggota> doInBackground() { try { return svc.semua(); } catch (Exception e) { err = e; return List.of(); } }
            protected void done() {
                setBusy(false); if (err != null) { err(err); return; }
                try {
                    List<Anggota> list = get();
                    String[] kol = {"ID","Nama","Alamat","Telepon","Status"};
                    Object[][] d = new Object[list.size()][5];
                    for (int i=0;i<list.size();i++){ Anggota a=list.get(i); d[i]=new Object[]{a.getId(),a.getNama(),a.getAlamat(),a.getTelepon(),a.getStatus()}; }
                    tabel.setModel(new DefaultTableModel(d, kol){ public boolean isCellEditable(int r,int c){return false;} });
                    if (tabel.getColumnModel().getColumnCount()>0) tabel.getColumnModel().getColumn(0).setMaxWidth(60);
                } catch (Exception e) { err(e); }
            }
        }.execute();
    }
}
