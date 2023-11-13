import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.Rectangle;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyListener;
import java.util.Random;
import java.awt.Font;

class Pair{
    public double x;
    public double y;

    public Pair(double initX, double initY){
        x = initX;
        y = initY;
    }

    public Pair add(Pair toAdd){

        return new Pair(x + toAdd.x, y + toAdd.y);
    }

    public Pair divide(double denom){

        return new Pair(x / denom, y / denom);
    }

    public Pair times(double val){

        return new Pair(x * val, y * val);
    }

    public void flipX(){
        x = -x;
    }

    public void flipY(){
        y = -y;
    }
}
class Paddle extends Rectangle {
    int whichpaddle;
    Pair velocity;

    Pair position;
    Pair acceleration;
    double padheight;
    double padwidth;
    static boolean keypressed = false;
    static boolean keypressed1 = false;
    static boolean keypressed2 = false;
    Pair velocity2;

    public Paddle(double x, double y, double width, double height, int whichpaddle){
        super((int)x,(int)y, (int) width, (int) height);
        position = new Pair(x,y);
        acceleration = new Pair(0.0,0.0);
        velocity = new Pair(0.0,0.0);
        padheight = height;
        padwidth = width;
        this.whichpaddle = whichpaddle;

    }
    public void keyPressed(KeyEvent e) {

        switch(whichpaddle){
            case 1:
                keypressed1 = true;
                if(e.getKeyChar() == 'r') {
                    setVelocity(new Pair(0.0,-300.0));
                    move();

                }
                if(e.getKeyChar() == 'v'){
                    setVelocity(new Pair(0.0,300.0));
                    move();
                }
                if(e.getKeyChar() == 'f'){
                    setVelocity(new Pair(0.0,0.0));
                    move();
                }
                break;
            case 2:
                keypressed2 = true;
                if(e.getKeyChar() == 'u'){
                    setVelocity(new Pair(0.0,-300.0));
                    move();

                }
                if(e.getKeyChar() == 'n'){
                    setVelocity(new Pair(0.0,300.0));
                    move();
                }
                if(e.getKeyChar() == 'j'){
                    setVelocity(new Pair(0.0,0.0));
                    move();
                }
                break;
        }


    }
    public void keyReleased(KeyEvent e) {

    }
    public void setPosition( Pair p){
        position = p;
    }
    public void setVelocity(Pair v){
        velocity = v;
    }
    public void setAcceleration(Pair a){

    }
    public Pair getPosition(){
        return position;
    }
    public Pair getVelocity(){
        return velocity;
    }
    public Pair getAcceleration(){
        return acceleration;
    }
    public void move(){
        position = position.add(velocity.times(1.0/150.0));
        velocity = velocity.add(acceleration.times(0.0));
    }
    public void draw(Graphics g){
        if(whichpaddle == 1){
            g.setColor(Color.cyan);
        }else{
            g.setColor(Color.blue);
        }
        g.fillRect((int) position.x, (int) position.y, (int) padwidth, (int) padheight);

    }
    public double getX(){
        return position.x;
    }
    public double getY() {
        return position.y;
    }

}
 class Ball {
    Pair velocity;
    Pair position;
    Pair acceleration;
    double diameter;

    public Ball(double x, double y, double diameter) {
        position = new Pair(x, y);
        this.diameter = diameter;
        Random rand = new Random();
        int initDirection = rand.nextInt(2);
        if (initDirection == 0) {
            velocity = new Pair(-250.0, 0.0);
        }
        if (initDirection == 1) {
            velocity = new Pair(250.0, 0.0);
        }
        acceleration = new Pair(0.0, 0.0);
    }

    public void setPosition(Pair p) {
        position = p;
    }

    public void setVelocity(Pair v) {

        velocity = v;
    }
    public void setYVelocity(double bally) {

        velocity.y = bally;
    }


    public void setAcceleration(Pair a) {

        acceleration = a;
    }

    public Pair getPosition() {

        return position;
    }

    public Pair getVelocity() {

        return velocity;
    }

    public Pair getAcceleration() {

        return acceleration;
    }

    public void move() {
        position = position.add(velocity.times(1.0 / 150.0));
        velocity = velocity.add(acceleration.times(0.0));
    }

    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillOval((int) position.x, (int) position.y, (int) this.diameter, (int) this.diameter);
    }

    public double getX() {
        return position.x;
    }

    public double getY() {
        return position.y;
    }
}
class Score extends Rectangle {
    public static int WIDTH;
    public static int HEIGHT;
    int player1;
    int player2;
    public Score(int WIDTH, int HEIGHT ){
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;

    }
    public void draw(Graphics g){
        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.BOLD,60));
        g.drawLine(WIDTH/2, 0, WIDTH/2, HEIGHT);
        g.setFont(new Font("Arial",Font.PLAIN,60));
        g.drawString(String.valueOf(player1/10)+String.valueOf(player1%10), (WIDTH/2)-85, 50);
        g.drawString(String.valueOf(player2/10)+String.valueOf(player2%10), (WIDTH/2)+20, 50);
        g.setColor(Color.RED);
        g.fillRect(0,0,100,100);

    }
}

class GamePanel extends JPanel implements Runnable {
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;
    public static final int FPS = 250;
    public static final Dimension PANEL_SIZE = new Dimension(WIDTH,HEIGHT);
    public static final int BALL_DIAMETER = 20;
    public static final int PADDLE_HEIGHT= 100;
    public static final int PADDLE_WIDTH= 25;
    Paddle Player1;
    Paddle Player2;
    Score score;
    Ball ball;
    Thread gameThread;
    Image image;
    Graphics graphics;


    public GamePanel(){
        resetBall();
        resetPaddles();
        score = new Score( WIDTH,HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new keylistener());
        this.setPreferredSize(PANEL_SIZE);


        gameThread = new Thread(this);
        gameThread.start();
    }
    public void resetBall(){
        ball = new Ball((WIDTH/2) - (BALL_DIAMETER/2),(HEIGHT/2) - (BALL_DIAMETER/2),BALL_DIAMETER);
    }
    public void resetPaddles(){

        Player1 = new Paddle(0,(HEIGHT/2) - (PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,1);
        Player2 = new Paddle(WIDTH-PADDLE_WIDTH,(HEIGHT/2) - (PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,2);

    }
    public void paint(Graphics g){
        image = createImage(getWidth(),getWidth());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image,0,0,this);
    }
    public void draw(Graphics g){
        this.Player1.draw(g);
        this.Player2.draw(g);
        ball.draw(g);
        score.draw(g);
    }
    public void move(){
        ball.move();
        if (Paddle.keypressed1 == true) {
            Player1.move();
        }
        if (Paddle.keypressed2 == true) {
            Player2.move();
        }

    }
    public void checkCollision(){
        //ball collision with the paddles
        if ((ball.getX() >= Player1.getX() && ball.getX() <= Player1.getX() + Player1.padwidth) &&
                (ball.getY() >= Player1.getY() && ball.getY() <= Player1.getY() + Player1.padheight)) {
            double distancefromcenter = (Player1.getX() + PADDLE_WIDTH / 2) - (ball.getX() + ball.diameter / 2);
            double ratioofintersection = distancefromcenter / (PADDLE_WIDTH / 2);
            double bounceAngle = ratioofintersection * Math.toRadians(75);
            ball.velocity.flipX();
            ball.setYVelocity((ball.velocity.x * Math.tan(bounceAngle)) );

        }
        else if ((ball.getX() >= Player2.getX() && ball.getX() <= Player2.getX() + Player2.padwidth) &&
                (ball.getY() >= Player2.getY() && ball.getY() <= Player2.getY() + Player2.padheight)) {
            double distancefromcenter = (Player1.getX() + PADDLE_WIDTH / 2) - (ball.getX() + ball.diameter / 2);
            double ratioofintersection =  distancefromcenter / (PADDLE_WIDTH / 2);
            double bounceAngle = ratioofintersection * Math.toRadians(75);
            ball.velocity.flipX();
            ball.setYVelocity(ball.velocity.x * Math.tan(bounceAngle) );
        }



        //ball collision with wall;
        if (ball.position.y - (ball.diameter/2) < 0){
            ball.velocity.flipY();
            ball.position.y = ball.diameter/2;

        }
        else if(ball.position.y + (ball.diameter/2) >  HEIGHT) {
            ball.velocity.flipY();
            ball.position.y = HEIGHT - (ball.diameter/2);

        }
        //paddle collision with with wall
        if(Player1.position.y<=0)
            Player1.position.y=0;
        if(Player1.position.y >= (HEIGHT-PADDLE_HEIGHT))
            Player1.position.y = HEIGHT-PADDLE_HEIGHT;
        if(Player2.position.y<=0)
            Player2.position.y=0;
        if(Player2.position.y >= (HEIGHT-PADDLE_HEIGHT))
            Player2.position.y = HEIGHT-PADDLE_HEIGHT;
        //give player 1 point and reset paddles and
        if(ball.position.x <=0) {
            score.player2++;
            resetPaddles();
            resetBall();
            ball.setVelocity(new Pair(-250.0,0.0));
            System.out.println("Player 1 "+ score.player1 + " : " + score.player2 + " Player 2");
        }
        if(ball.position.x >= WIDTH-BALL_DIAMETER) {
            score.player1++;
            resetPaddles();
            resetBall();
            ball.setVelocity(new Pair(250.0,0.0));
            System.out.println("Player 1 "+ score.player1 + " : " + score.player2 + " Player 2");
        }
    }
    public void run(){
        while(true) {

            checkCollision();
            move();
            repaint();

            try {
                Thread.sleep(1000 / FPS);
            } catch (InterruptedException e) {


            }
        }
    }
    public class keylistener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            //  System.out.println("STEP");
            Player1.keyPressed(e);
            Player2.keyPressed(e);

        }

        @Override
        public void keyReleased(KeyEvent e) {
            // System.out.println("STEP");
            Player1.keyReleased(e);
            Player2.keyReleased(e);
        }
    }
}

public class Pong {
    public static void main(String[] args) {
        GameFrame pongframe = new GameFrame();
    }
}



class GameFrame extends JFrame {
    GamePanel panel;
    public GameFrame(){
        panel = new GamePanel();
        this.add(panel);
        this.setTitle("Pong");
        this.setResizable(false);
        this.setBackground(Color.GREEN);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }

}

