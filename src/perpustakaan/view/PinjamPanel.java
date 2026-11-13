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

    public PinjamPanel(Petugas p) {
        this.petugas = p;
        setLayout(new BorderLayout(15,15));
        setBackground(Tema.LATAR);
        setBorder(BorderFactory.createEmptyBorder(20,25,25,25));
        add(atas(), BorderLayout.NORTH);
        add(scroll(), BorderLayout.CENTER);
    }

    private JPanel atas() {
        JPanel p = new JPanel(new GridLayout(2,2,10,8)); p.setOpaque(false);
        p.add(field("Anggota", cbAnggota)); p.add(field("Buku", cbBuku));
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT,8,0)); btns.setOpaque(false);
        JButton b1=new JButton("Pinjam"), b2=new JButton("Kembalikan"), b3=new JButton("Refresh");
        b1.addActionListener(e->pinjam()); b2.addActionListener(e->kembali()); b3.addActionListener(e->muat());
        btns.add(b1); btns.add(b2); btns.add(b3);
        p.add(btns); p.add(new JLabel());
        return p;
    }

    private JPanel field(String l, JComponent c) {
        JPanel p = new JPanel(new BorderLayout(4,4)); p.setOpaque(false);
        JLabel lb = new JLabel(l); lb.setFont(Tema.FONT_TEBAL); lb.setForeground(Tema.TEKS);
        p.add(lb, BorderLayout.NORTH); p.add(c, BorderLayout.CENTER); return p;
    }

    private JScrollPane scroll() {
        tabel.setFont(Tema.FONT_BIASA); tabel.setRowHeight(28); tabel.getTableHeader().setFont(Tema.FONT_TEBAL);
        JScrollPane sp = new JScrollPane(tabel); sp.setBorder(BorderFactory.createLineBorder(Tema.GARIS));
        sp.getViewport().setBackground(Tema.PUTIH); return sp;
    }

    private void pinjam(){
        try{
            Anggota a=(Anggota)cbAnggota.getSelectedItem(); Buku b=(Buku)cbBuku.getSelectedItem();
            if(a==null||b==null){JOptionPane.showMessageDialog(this,"Pilih anggota dan buku");return;}
            svc.pinjam(a.getId(), b.getId(), petugas.getId()); muat();
        }catch(Exception e){ err(e); }
    }
    private void kembali(){
        int r=tabel.getSelectedRow(); if(r<0){JOptionPane.showMessageDialog(this,"Pilih transaksi");return;}
        try{
            if("Kembali".equalsIgnoreCase(String.valueOf(tabel.getValueAt(r,5)))){JOptionPane.showMessageDialog(this,"Sudah kembali");return;}
            svc.kembali((Integer)tabel.getValueAt(r,0)); muat();
        }catch(Exception e){ err(e); }
    }
    private void err(Exception e){ e.printStackTrace(); JOptionPane.showMessageDialog(this,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE); }

    @Override public void muat(){
        try{
            cbAnggota.removeAllItems(); for(Anggota a: svc.anggotaAktif()) cbAnggota.addItem(a);
            cbBuku.removeAllItems(); for(Buku b: svc.bukuAda()) cbBuku.addItem(b);
            List<Pinjam> list = svc.semua();
            String[] kol={"ID","Buku","Anggota","Tgl Pinjam","Tgl Kembali","Status"};
            Object[][] d=new Object[list.size()][6];
            for(int i=0;i<list.size();i++){
                Pinjam x=list.get(i);
                d[i]=new Object[]{x.getId(), x.getBuku()!=null?x.getBuku().getJudul():"-", x.getAnggota()!=null?x.getAnggota().getNama():"-",
                        x.getTglPinjam()!=null?x.getTglPinjam().format(fmt):"-", x.getTglKembali()!=null?x.getTglKembali().format(fmt):"-", x.getStatus()};
            }
            tabel.setModel(new DefaultTableModel(d,kol){ public boolean isCellEditable(int r,int c){return false;} });
        }catch(Exception e){ err(e); }
    }
}
