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
    private final JTextField fJudul = new JTextField(), fPengarang = new JTextField(),
            fPenerbit = new JTextField(), fTahun = new JTextField(), fStok = new JTextField();
    private final JTable tabel = new JTable();
    private Integer id = null;

    public BukuPanel() {
        setLayout(new BorderLayout(15,15));
        setBackground(Tema.LATAR);
        setBorder(BorderFactory.createEmptyBorder(20,25,25,25));
        add(form(), BorderLayout.NORTH);
        add(scroll(), BorderLayout.CENTER);
        tabel.getSelectionModel().addListSelectionListener(e -> {
            if(e.getValueIsAdjusting()) return;
            int r = tabel.getSelectedRow(); if(r<0) return;
            id = (Integer) tabel.getValueAt(r,0);
            fJudul.setText(v(r,1)); fPengarang.setText(v(r,2)); fPenerbit.setText(v(r,3));
            fTahun.setText(v(r,4)); fStok.setText(v(r,5));
        });
    }

    private JPanel form() {
        JPanel p = new JPanel(new GridLayout(3,2,10,8)); p.setOpaque(false);
        p.add(field("Judul", fJudul)); p.add(field("Pengarang", fPengarang));
        p.add(field("Penerbit", fPenerbit)); p.add(field("Tahun", fTahun));
        JPanel stok = field("Stok", fStok);
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT,8,18)); btns.setOpaque(false);
        JButton b1=new JButton("Tambah"), b2=new JButton("Ubah"), b3=new JButton("Hapus"), b4=new JButton("Clear");
        b1.addActionListener(e->tambah()); b2.addActionListener(e->ubah()); b3.addActionListener(e->hapus()); b4.addActionListener(e->clear());
        btns.add(b1); btns.add(b2); btns.add(b3); btns.add(b4);
        JPanel wrap = new JPanel(new BorderLayout(10,0)); wrap.setOpaque(false);
        wrap.add(stok, BorderLayout.CENTER); wrap.add(btns, BorderLayout.EAST);
        JPanel all = new JPanel(new BorderLayout(0,8)); all.setOpaque(false);
        all.add(p, BorderLayout.CENTER); all.add(wrap, BorderLayout.SOUTH);
        return all;
    }

    private JPanel field(String l, JTextField f) {
        JPanel p = new JPanel(new BorderLayout(4,4)); p.setOpaque(false);
        JLabel lb = new JLabel(l); lb.setFont(Tema.FONT_TEBAL); lb.setForeground(Tema.TEKS);
        f.setFont(Tema.FONT_BIASA);
        p.add(lb, BorderLayout.NORTH); p.add(f, BorderLayout.CENTER); return p;
    }

    private JScrollPane scroll() {
        tabel.setFont(Tema.FONT_BIASA); tabel.setRowHeight(28); tabel.getTableHeader().setFont(Tema.FONT_TEBAL);
        JScrollPane sp = new JScrollPane(tabel); sp.setBorder(BorderFactory.createLineBorder(Tema.GARIS));
        sp.getViewport().setBackground(Tema.PUTIH); return sp;
    }

    private String v(int r,int c){ Object o=tabel.getValueAt(r,c); return o==null?"":o.toString(); }

    private void tambah(){ try{ svc.simpan(fJudul.getText(),fPengarang.getText(),fPenerbit.getText(),fTahun.getText(),fStok.getText()); clear(); muat(); }catch(Exception e){ err(e);} }
    private void ubah(){ if(id==null){JOptionPane.showMessageDialog(this,"Pilih data");return;} try{ svc.ubah(id,fJudul.getText(),fPengarang.getText(),fPenerbit.getText(),fTahun.getText(),fStok.getText()); clear(); muat(); }catch(Exception e){ err(e);} }
    private void hapus(){ if(id==null){JOptionPane.showMessageDialog(this,"Pilih data");return;} if(JOptionPane.showConfirmDialog(this,"Hapus?","Konfirmasi",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return; try{ svc.hapus(id); clear(); muat(); }catch(Exception e){ err(e);} }
    private void clear(){ id=null; fJudul.setText("");fPengarang.setText("");fPenerbit.setText("");fTahun.setText("");fStok.setText(""); tabel.clearSelection(); }
    private void err(Exception e){ e.printStackTrace(); JOptionPane.showMessageDialog(this,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE); }

    @Override public void muat(){
        try{
            List<Buku> list = svc.semua();
            String[] kol={"ID","Judul","Pengarang","Penerbit","Tahun","Stok"};
            Object[][] d=new Object[list.size()][6];
            for(int i=0;i<list.size();i++){ Buku b=list.get(i); d[i]=new Object[]{b.getId(),b.getJudul(),b.getPengarang(),b.getPenerbit(),b.getTahun(),b.getStok()}; }
            tabel.setModel(new DefaultTableModel(d,kol){ public boolean isCellEditable(int r,int c){return false;} });
        }catch(Exception e){ err(e); }
    }
}
