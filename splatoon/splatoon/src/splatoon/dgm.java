package splatoon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class dgm {
	public ArrayList<packet> p = new ArrayList<packet>();
	public ArrayList<packet> t = new ArrayList<packet>();
	public ArrayList<packet> r = new ArrayList<packet>();
	public long pos = 0;
	public int range = 30;
	public double fps = 58.0d;
	public ArrayList<Ipcount> iplist = new ArrayList<Ipcount>();
	public ArrayList<String> ip = new ArrayList<String>();
	public void next(){
		r.clear();
		t.clear();
		iplist.clear();
		pos += (1.0d/fps)*1000.0d;
		double end = pos+range;
		for(int i = 0;i<p.size();i++){
			packet ind = p.get(i);
			if(ind.nip==null)continue;
			if(ind.time>=pos & ind.time<end){
				r.add(ind);
				t.add(ind);
			}
		}
		for(int i = 0;i < r.size();i++){
			packet l = r.get(i);
			if(!l.dis_addr.startsWith("192.168.")){
				boolean foundit = false;
				for(int ot = 0;ot <iplist.size();ot++){
					if(iplist.get(ot).ip.compareTo(l.dis_addr)==0){
						iplist.get(ot).outcount++;
						iplist.get(ot).outsize+=l.data.length;
						foundit=true;
						break;
					}
				}
				if(!foundit){
					Ipcount newip = new Ipcount();
					newip.outcount=1;
					newip.outsize=l.data.length;
					newip.ip = l.dis_addr;
					iplist.add(newip);
				}
			}else{
				boolean foundit = false;
				for(int ot = 0;ot <iplist.size();ot++){
					if(iplist.get(ot).ip.compareTo(l.src_addr)==0){
						iplist.get(ot).incount++;
						iplist.get(ot).insize+=l.data.length;
						foundit=true;
						break;
					}
				}
				if(!foundit){
					Ipcount newip = new Ipcount();
					newip.incount=1;
					newip.insize=l.data.length;
					newip.ip = l.src_addr;
					iplist.add(newip);
				}
			}
		}
		Collections.sort(iplist, new Comparator<Ipcount>(){
			@Override public int compare(Ipcount arg0, Ipcount arg1) {
				return arg0.ip.compareTo(arg1.ip);
			}
		});
		Collections.sort(r, new Comparator<packet>(){
			@Override public int compare(packet arg0, packet arg1) {
				if(arg0.src_addr.startsWith("192.168.")){
					if(arg1.src_addr.startsWith("192.168."))
						return arg0.dis_addr.compareTo(arg1.dis_addr);
					return arg0.dis_addr.compareTo(arg1.src_addr);
				}else{
					if(arg1.src_addr.startsWith("192.168."))
						return arg0.src_addr.compareTo(arg1.dis_addr);
					return arg0.src_addr.compareTo(arg1.src_addr);
				}
			}
		});
	}
}