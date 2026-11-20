package perpustakaan.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import perpustakaan.App;
import perpustakaan.Muat;
import perpustakaan.Tema;
import perpustakaan.model.Anggota;
import perpustakaan.model.Buku;
import perpustakaan.model.Pinjam;
import perpustakaan.model.Petugas;
import perpustakaan.service.PinjamService;

public class PinjamPanel extends JPanel implements Muat {
    private final PinjamService svc = App.get(PinjamService.class);
    private final Petugas petugas;
    private final JComboBox<Anggota> cbAnggota = new JComboBox<>();
    private final JComboBox<Buku> cbBuku = new JComboBox<>();
    private final JTable tabel = new JTable();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final JButton bPinjam = Tema.tombol("Pinjam");
    private final JButton bKembali = Tema.tombol("Kembalikan");
    private final JButton bRefresh = Tema.tombolNetral("Muat ulang");

    public PinjamPanel(Petugas p) {
        this.petugas = p;
        setLayout(new BorderLayout(0, 16));
        setBackground(Tema.LATAR);
        setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));
        add(atas(), BorderLayout.NORTH);
        add(tabelBox(), BorderLayout.CENTER);
        cbAnggota.setFont(Tema.FONT_BIASA); cbBuku.setFont(Tema.FONT_BIASA);
        cbAnggota.setBackground(Tema.PUTIH); cbBuku.setBackground(Tema.PUTIH);
        Tema.styleTabel(tabel);
        bPinjam.addActionListener(e -> pinjam());
        bKembali.addActionListener(e -> kembali());
        bRefresh.addActionListener(e -> muat());
    }

    private JPanel atas() {
        Tema.Kartu k = new Tema.Kartu(Tema.PUTIH);
        k.setLayout(new GridBagLayout());
        k.setBorder(BorderFactory.createCompoundBorder(Tema.garis(), BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6); c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;

        c.gridx = 0; c.gridy = 0; k.add(field("Anggota", cbAnggota), c);
        c.gridx = 1; c.gridy = 0; k.add(field("Buku", cbBuku), c);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btns.setOpaque(false);
        btns.add(bPinjam); btns.add(bKembali); btns.add(bRefresh);
        c.gridx = 0; c.gridy = 1; c.gridwidth = 2; c.weightx = 0; c.insets = new Insets(10, 6, 4, 6);
        k.add(btns, c);
        return k;
    }

    private JPanel field(String label, JComponent comp) {
        JPanel w = new JPanel(new BorderLayout(0, 4)); w.setOpaque(false);
        JLabel l = new JLabel(label); l.setFont(Tema.FONT_TEBAL); l.setForeground(Tema.TEKS);
        w.add(l, BorderLayout.NORTH); w.add(comp, BorderLayout.CENTER);
        return w;
    }

    private JComponent tabelBox() {
        Tema.Kartu k = new Tema.Kartu(Tema.PUTIH);
        k.setLayout(new BorderLayout());
        k.setBorder(BorderFactory.createCompoundBorder(Tema.garis(), BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        JScrollPane sp = new JScrollPane(tabel); sp.setBorder(Tema.garis()); sp.getViewport().setBackground(Tema.PUTIH);
        k.add(sp, BorderLayout.CENTER); return k;
    }

    private void setBusy(boolean b) {
        bPinjam.setEnabled(!b); bKembali.setEnabled(!b); bRefresh.setEnabled(!b);
        setCursor(b ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor());
    }
    private void err(Exception e) { e.printStackTrace(); JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }

    private void pinjam() {
        Anggota a = (Anggota) cbAnggota.getSelectedItem();
        Buku b = (Buku) cbBuku.getSelectedItem();
        if (a == null || b == null) { JOptionPane.showMessageDialog(this, "Pilih anggota dan buku"); return; }
        Integer idA = a.getId(), idB = b.getId(), idP = petugas.getId();
        setBusy(true);
        new SwingWorker<Void, Void>() {
            Exception err;
            protected Void doInBackground() { try { svc.pinjam(idA, idB, idP); } catch (Exception e) { err = e; } return null; }
            protected void done() { setBusy(false); if (err != null) err(err); else muat(); }
        }.execute();
    }

    private void kembali() {
        int r = tabel.getSelectedRow();
        if (r < 0) { JOptionPane.showMessageDialog(this, "Pilih transaksi"); return; }
        if ("Kembali".equalsIgnoreCase(String.valueOf(tabel.getValueAt(r, 5)))) { JOptionPane.showMessageDialog(this, "Sudah kembali"); return; }
        int idPinjam = (Integer) tabel.getValueAt(r, 0);
        setBusy(true);
        new SwingWorker<Void, Void>() {
            Exception err;
            protected Void doInBackground() { try { svc.kembali(idPinjam); } catch (Exception e) { err = e; } return null; }
            protected void done() { setBusy(false); if (err != null) err(err); else muat(); }
        }.execute();
    }

    @Override
    public void muat() {
        setBusy(true);
        new SwingWorker<Data, Void>() {
            Exception err;
            protected Data doInBackground() {
                try { Data d = new Data(); d.anggota = svc.anggotaAktif(); d.buku = svc.bukuAda(); d.list = svc.semua(); return d; }
                catch (Exception e) { err = e; return null; }
            }
            protected void done() {
                setBusy(false);
                if (err != null) { err(err); return; }
                try {
                    Data d = get();
                    Object sa = cbAnggota.getSelectedItem(), sb = cbBuku.getSelectedItem();
                    cbAnggota.removeAllItems(); for (Anggota a : d.anggota) cbAnggota.addItem(a);
                    cbBuku.removeAllItems(); for (Buku b : d.buku) cbBuku.addItem(b);
                    String[] kol = {"ID","Buku","Anggota","Tgl Pinjam","Tgl Kembali","Status"};
                    Object[][] rows = new Object[d.list.size()][6];
                    for (int i=0;i<d.list.size();i++) {
                        Pinjam x = d.list.get(i);
                        rows[i] = new Object[]{x.getId(), x.getBuku()!=null?x.getBuku().getJudul():"-", x.getAnggota()!=null?x.getAnggota().getNama():"-",
                                x.getTglPinjam()!=null?x.getTglPinjam().format(fmt):"-", x.getTglKembali()!=null?x.getTglKembali().format(fmt):"-", x.getStatus()};
                    }
                    tabel.setModel(new DefaultTableModel(rows, kol){ public boolean isCellEditable(int r,int c){return false;} });
                    if (tabel.getColumnModel().getColumnCount()>0) tabel.getColumnModel().getColumn(0).setMaxWidth(60);
                } catch (Exception e) { err(e); }
            }
        }.execute();
    }

    private static class Data {
        List<Anggota> anggota = List.of();
        List<Buku> buku = List.of();
        List<Pinjam> list = List.of();
    }
}
