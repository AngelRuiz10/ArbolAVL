

package ArbolBalance;

//Librerias a utilizar para el funcionamiento correcto del programa
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
//Angel Abrahan Ruiz Avila
//0901-16-17865
//Clase que crea el nodos nodos izq y der, inicializando el padre a null

class AVLTree{
	class Node{
		int N;
		int altura=0;
		Node parent=null,izquierda=null,derecha=null;
		Node(int n,int h,Node P){
			N=n;
			altura=h;
			parent=P;
		}
	}

	Node root=null;
	void insert(int N,Node R){
		if(root==null)
			root=new Node(N,1,null);
		else{
			if(N<=R.N){
				if(R.izquierda==null){
					R.izquierda=new Node(N,1,R);
					dobalance(R.izquierda);
				}else
					insert(N,R.izquierda);
			}else{
				if(R.derecha==null){
					R.derecha=new Node(N,1,R);
					dobalance(R.derecha);
				}else
					insert(N,R.derecha);
			}
		}
	}
	
	void remove(Node x){
		Node y,z=null;
		if(x.izquierda==null || x.derecha==null)//al menos un niÃ±o
			y=x;
		else{
                //encontrando sucesor
			Node temp=x;
			for(y=temp.parent;y!=null && temp==y.derecha;y=y.parent){
				temp=y;				
			}
		}	//hijo o no hijo

	//Si tiene por lo menos un hijo
		if(y.izquierda!=null)
			z=y.izquierda;
		else
			z=y.derecha;
			
		if(z!=null)							
			z.parent=y.parent;
		
		if(y.parent==null)                                    //z era root
			root=z;
		else{                                                 //eliminando y
			if(y==y.parent.izquierda)                     //estaba a la izquierda
				y.parent.izquierda=z;	
			else
				y.parent.derecha=z;                   //estaba en la derecha
		}
		x.N=y.N;                                              //copiando y a x
		y.altura=0;
		
		do{
			y=dobalance(y);
		}while(y!=null);				      //equilibrio hasta la root
	}
            /************
            Reformando el arbol despues de la eliminacion o insercion
            operacion							***********/
	Node dobalance(Node x){
		int h1=0,h2=0;					      //aqui­ h1 altura de x, h2 altura de x`s hermano
		Node n1=null,rent=null;
		Node y=x,z;
		while(y.parent!=null){
			if(y.parent.izquierda==y) n1=y.parent.derecha;
			else if(y.parent.derecha==y) n1=y.parent.izquierda;
			h1=y.altura;
			if(n1==null)
				h2=0;			
			else
				h2=n1.altura;
			if(Math.abs(h2-h1)>1)								
				break;
			y.parent.altura=1+Math.max(h1,h2);
			y=y.parent;
		}
		
		if(y.parent==null) 
			return null;		

		z=y.parent;
		rent=z;		
	       /* vinculando z a y e y a x. tal que y y x tiene maximo
                altura entre hermanos respectivamente */
		h1=(z.izquierda==null)?0:z.izquierda.altura;
		h2=(z.derecha==null)?0:z.derecha.altura;
		if(h1<h2)
			y=z.derecha;
		else
			y=z.izquierda;

		h1=(y.izquierda==null)?0:y.izquierda.altura;
		h2=(y.derecha==null)?0:y.derecha.altura;
		if(h1<h2)
			x=y.derecha;
		else
			x=y.izquierda;
		
		y.parent=z; 
		x.parent=y;		

	        //Encontrar el tipo de rotacion necesaria para equilibrar el Arbol.
		if(z.izquierda==y){
			if(y.izquierda==x)
				Rrotation(y,z);		//rotacionn de una sola izquierda
			else{
				Lrotation(x,y);		//rotacionn doble izquierda
				x.altura++;
				Rrotation(x,z);			
			}		
		}else{
			if(y.derecha==x){
				Lrotation(y,z);		//solo rotacionn derecha
			}else{
				Rrotation(x,y);		//rotacionn doble derecha
				x.altura++;
				Lrotation(x,z);
			}
		}
		
		return rent;
	}
	
              /*************
	
             RotaciÃ³n a la derecha que toma dos nodos y gira en segundo lugar como primer centro de toma
																		***********/
	void Rrotation(Node y,Node z){
	//vinculando y con el padre original de z
		y.parent=z.parent;
		if(y.parent==null)
			root=y;
		else if(y.parent.izquierda==z)
			y.parent.izquierda=y;
		else
			y.parent.derecha=y;

	       //rotaciÃ³n b / w y n z
		z.izquierda=y.derecha;
		if(z.izquierda!=null)
			z.izquierda.parent=z;
		y.derecha=z;
		z.parent=y;
		z.altura--;
	}

                /*************
	     RotaciÃ³n a la izquierda que toma dos nodos y gira en segundo lugar como primer centro de toma
																		***********/
	void Lrotation(Node y,Node z){
	     //vinculando y con el padre original de z
		y.parent=z.parent;
		if(z.parent==null)
			root=y;
		else if(z.parent.izquierda==z)
			y.parent.izquierda=y;
		else
			y.parent.derecha=y;

	       //rotaciÃ³n b / w x n y
		z.derecha=y.izquierda;
		if(z.derecha!=null)
			z.derecha.parent=z;
		y.izquierda=z;
		z.parent=y;		
		z.altura--;
	}

	
                /************
              Para buscar el nodo desde el Ã¡rbol
                **********/
	Node search(int N,Node r){
		if(r==null) return null;
		if(N==r.N)
			return r;
		else if(N<r.N)
			return search(N,r.izquierda);
		else 
			return search(N,r.derecha);
	}
	
	static AVLTree B;
	public static void main(String[] args){
		B=new AVLTree();
		new Main();
	}			
			
			
	     //Clase Principal
            /**** Clases de grÃ¡ficos y GUI ***/
	static class Main extends JFrame implements ActionListener{
		/**
		 * 
		 */
		private static final long serialVersionUID = -2829448395694197965L;

		public Main(){
			this.setSize(500,200);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setLocationRelativeTo(null);
			this.setTitle("Arbol AVL");
			panel1 = new JPanel();
		        /*************botones*****/
			button0 = new JButton("Insertar"); 
			button1 = new JButton("Buscar");
			button2 = new JButton("Eliminar");
			button3 = new JButton("Mostrar Arbol");
			button0.addActionListener(this); 
			button1.addActionListener(this);
			button2.addActionListener(this);
			button3.addActionListener(this);
			panel1.add(button0);
			panel1.add(button1);
			panel1.add(button2);
			panel1.add(button3);
			
			Border b = BorderFactory.createEmptyBorder(45,0,0, 0);
			panel1.setBorder(b);	
			this.add(panel1);			
			this.setVisible(true);	
		}
		JPanel panel1;
		private JButton button0,button1,button2,button3;

		public void actionPerformed(ActionEvent e){		
			if (e.getSource() == button0){
				String s=JOptionPane.showInputDialog("Ingrese el valor entero");
				int i=Integer.parseInt(s);
				B.insert(i,B.root);
			}else if(e.getSource() == button1){
				String s=JOptionPane.showInputDialog("Ingrese el valor entero");
				int i=Integer.parseInt(s);
				if(B.search(i,B.root)==null)
					JOptionPane.showMessageDialog(null,"No Encontrado");
				else
					JOptionPane.showMessageDialog(null,"Encontrado");
			}else if(e.getSource() == button2){
				String s=JOptionPane.showInputDialog("Ingrese el valor entero");
				int i=Integer.parseInt(s);
				Node temp=B.search(i,B.root);
				if(temp==null)
					JOptionPane.showMessageDialog(null,"No Encontrado");
				else{
					B.remove(temp);
					JOptionPane.showMessageDialog(null,"Removido");
				}
			}else if(e.getSource() == button3){
				JFrame f = new JFrame("AVL Tree");
				f.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) { }
				});
				Drawtree applet = new Drawtree();
				f.getContentPane().add("Center", applet);
				Toolkit tk = Toolkit.getDefaultToolkit();  
				int xSize = ((int) tk.getScreenSize().getWidth()); 
				applet.init(B.root,xSize-50);
				f.pack();
				f.setSize(new Dimension(xSize,500));
				f.setVisible(true);
			}
		}
	}
	
         /************
	 arbol de dibujo usando graficos 2d de clase java
									******************/
        static class Drawtree extends JApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7654352523443329890L;
	final  Color bg = Color.white;
	final  Color fg = Color.black;
        final  Color red = Color.red;
        final  Color white = Color.white;
        final  BasicStroke stroke = new BasicStroke(2.0f);
        final  BasicStroke wideStroke = new BasicStroke(8.0f);
        Dimension totalSize;
        int altura,width;
        Node r=null;
        public void init(Node N,int x) {
		//Inicializar colores de dibujo.
		setBackground(bg);
		setForeground(fg);
		r=N;
		width=x;
	}
	Graphics2D g2;
        

        public void paint(Graphics g) {
		g2=  (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                getSize();
                inorder(r,0,width,80);
        }
	
	public void draw(int x1,int x2,int y,String n,int d){
	g2.setStroke(stroke);

	g2.setPaint(Color.black);
	int x=(x1+x2)/2;
	if(d==1)
		g2.draw(new Line2D.Double(x2, y-30, x+15, y));
	else if(d==2)
		g2.draw(new Line2D.Double(x+15, y,x1+30 , y-30));
	g2.setPaint(Color.blue);
	Shape circle=new Ellipse2D.Double((x1+x2)/2,y, 30, 30);
        g2.draw(circle);
	g2.fill(circle);
	g2.setPaint(Color.white);
        g2.drawString(n, x+10, y+18);
	}

	int x1=500,y1=30;
	void inorder(Node r,int x1,int x2,int y){
		if(r==null) return;
			
			inorder(r.izquierda,x1,(x1+x2)/2,y+40);
			if(r.parent==null) draw(x1,x2,y,r.N+"",0);
			else{
				if(r.parent.N<r.N)	draw(x1,x2,y,r.N+"",2);	
				else			draw(x1,x2,y,r.N+"",1);	
			}	
			inorder(r.derecha,(x1+x2)/2,x2,y+40);
        }
        }
}

