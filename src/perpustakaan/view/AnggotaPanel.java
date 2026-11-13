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
    private final JTextField fNama = new JTextField(), fAlamat = new JTextField(), fTelp = new JTextField();
    private final JComboBox<String> fStatus = new JComboBox<>(new String[]{"Aktif","Nonaktif"});
    private final JTable tabel = new JTable();
    private Integer id = null;

    public AnggotaPanel() {
        setLayout(new BorderLayout(15,15));
        setBackground(Tema.LATAR);
        setBorder(BorderFactory.createEmptyBorder(20,25,25,25));
        add(form(), BorderLayout.NORTH);
        add(scroll(), BorderLayout.CENTER);
        tabel.getSelectionModel().addListSelectionListener(e -> {
            if(e.getValueIsAdjusting()) return;
            int r = tabel.getSelectedRow(); if(r<0) return;
            id = (Integer) tabel.getValueAt(r,0);
            fNama.setText(v(r,1)); fAlamat.setText(v(r,2)); fTelp.setText(v(r,3)); fStatus.setSelectedItem(v(r,4));
        });
    }

    private JPanel form() {
        JPanel p = new JPanel(new GridLayout(2,2,10,8)); p.setOpaque(false);
        p.add(field("Nama", fNama)); p.add(field("Alamat", fAlamat));
        p.add(field("Telepon", fTelp));
        JPanel st = new JPanel(new BorderLayout(4,4)); st.setOpaque(false);
        JLabel lb = new JLabel("Status"); lb.setFont(Tema.FONT_TEBAL); lb.setForeground(Tema.TEKS);
        st.add(lb, BorderLayout.NORTH); st.add(fStatus, BorderLayout.CENTER);
        p.add(st);
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT,8,0)); btns.setOpaque(false);
        JButton b1=new JButton("Tambah"), b2=new JButton("Ubah"), b3=new JButton("Hapus"), b4=new JButton("Clear");
        b1.addActionListener(e->tambah()); b2.addActionListener(e->ubah()); b3.addActionListener(e->hapus()); b4.addActionListener(e->clear());
        btns.add(b1); btns.add(b2); btns.add(b3); btns.add(b4);
        JPanel all = new JPanel(new BorderLayout(0,8)); all.setOpaque(false);
        all.add(p, BorderLayout.CENTER); all.add(btns, BorderLayout.SOUTH);
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
    private void tambah(){ try{ svc.simpan(fNama.getText(),fAlamat.getText(),fTelp.getText(),(String)fStatus.getSelectedItem()); clear(); muat(); }catch(Exception e){ err(e);} }
    private void ubah(){ if(id==null){JOptionPane.showMessageDialog(this,"Pilih data");return;} try{ svc.ubah(id,fNama.getText(),fAlamat.getText(),fTelp.getText(),(String)fStatus.getSelectedItem()); clear(); muat(); }catch(Exception e){ err(e);} }
    private void hapus(){ if(id==null){JOptionPane.showMessageDialog(this,"Pilih data");return;} if(JOptionPane.showConfirmDialog(this,"Hapus?","Konfirmasi",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return; try{ svc.hapus(id); clear(); muat(); }catch(Exception e){ err(e);} }
    private void clear(){ id=null; fNama.setText("");fAlamat.setText("");fTelp.setText(""); fStatus.setSelectedIndex(0); tabel.clearSelection(); }
    private void err(Exception e){ e.printStackTrace(); JOptionPane.showMessageDialog(this,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE); }

    @Override public void muat(){
        try{
            List<Anggota> list = svc.semua();
            String[] kol={"ID","Nama","Alamat","Telepon","Status"};
            Object[][] d=new Object[list.size()][5];
            for(int i=0;i<list.size();i++){ Anggota a=list.get(i); d[i]=new Object[]{a.getId(),a.getNama(),a.getAlamat(),a.getTelepon(),a.getStatus()}; }
            tabel.setModel(new DefaultTableModel(d,kol){ public boolean isCellEditable(int r,int c){return false;} });
        }catch(Exception e){ err(e); }
    }
}
