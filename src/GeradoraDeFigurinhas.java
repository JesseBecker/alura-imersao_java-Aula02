import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class GeradoraDeFigurinhas {
    
    public void cria(InputStream inputStream, String nomeArquivo, String texto, InputStream inputStreamEmoji) throws Exception {

        // leitura da imagem
        //InputStream inputStream = new FileInputStream(new File("entrada/filme.jpg"));
        //InputStream inputStream = new URL("https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/TopMovies_1.jpg").openStream();
        BufferedImage imagemOriginal = ImageIO.read(inputStream);

        // cria nova imagem em memória com transparência e com tamanho novo
        int largura = imagemOriginal.getWidth();
        int altura = imagemOriginal.getHeight();
        int novaAltura = altura + 250;
        BufferedImage novaImagem = new BufferedImage(largura, novaAltura, BufferedImage.TRANSLUCENT);

        // copiar a imagem pra nova imagem (em memória)
        Graphics2D graphics = (Graphics2D) novaImagem.getGraphics();
        graphics.drawImage(imagemOriginal, 0, 0, null);

        //Desafio_5 Aula_2
        BufferedImage imagemEmoji = ImageIO.read(inputStreamEmoji);
        int posicaoImagemEmojiY = novaImagem.getHeight() - imagemEmoji.getHeight();
        graphics.drawImage(imagemEmoji, 0, posicaoImagemEmojiY - 22, null);



        // configurar a fonte
        var fonte = new Font("Impact", Font.BOLD, 182); //Desafio_3 Aula_2
        graphics.setColor(Color.blue);
        graphics.setFont(fonte);

        // escrever uma frase na nova imagem 
        // String texto = "TOPZERA";                                            //Desafio_2 Aula_2 //Desafio_5 Aula_2
        FontMetrics fontMetrics = graphics.getFontMetrics();                    //Desafio_2 Aula_2
        Rectangle2D retangulo = fontMetrics.getStringBounds(texto, graphics);   //Desafio_2 Aula_2
        int larguraTexto = (int) retangulo.getWidth();                          //Desafio_2 Aula_2
        int posicaoTextoX = (largura - larguraTexto) / 2;                       //Desafio_2 Aula_2
        int posicaoTextoY = novaAltura - 50;                                    //Desafio_4 Aula_2
        graphics.drawString(texto, posicaoTextoX, posicaoTextoY);               //Desafio_2 Aula_2

        //Desafio_4 Aula_2 ->
        FontRenderContext fontRenderContext = graphics.getFontRenderContext();
        var textLayout = new TextLayout(texto, fonte, fontRenderContext);

        Shape outline = textLayout.getOutline(null);
        AffineTransform transform = graphics.getTransform();
        transform.translate(posicaoTextoX, posicaoTextoY);
        graphics.setTransform(transform);

        var outlineStroke = new BasicStroke(largura * 0.015f);
        graphics.setStroke(outlineStroke);

        graphics.setColor(Color.BLACK);
        graphics.draw(outline);
        graphics.setClip(outline);
        //Desafio_4 Aula_2 <-

        // escrever a imagem nova em um arquivo
        ImageIO.write(novaImagem, "png", new File(nomeArquivo));

    }

}
