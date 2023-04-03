import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map;

public class App {

    public static void main(String[] args) throws Exception {

        //fazer uma conexão HTTP e buscar os top filmes e/ou top  series
        //String url = "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/TopMovies.json"; //endereço alternativo
        //String url = "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/TopTVs.json"; //Desafio_1 Aula_1
        //String url = "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/MostPopularMovies.json"; //Desafio_5 Aula_2
        //String url = "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/MostPopularTVs.json"; //Desafio_1 Aula_1
        String imdbKey = System.getenv("IMDB_API_KEY"); //Desafio_3 Aula_1
        String url = imdbKey;                                //Desafio_3 Aula_1
        URI endereco = URI.create(url);
        HttpClient client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(endereco).GET().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String body = response.body();

        //pegar só os dados que interessam (titulo, poster, classificação)
        var parser = new JsonParser();
        List<Map<String, String>> listaDeFilmes = parser.parse(body); 

        //exibir e manipular os dados
        /* Original
        for (Map<String,String> filme : listaDeFilmes) {
            System.out.println(filme.get("title"));
            System.out.println(filme.get("image"));
            System.out.println(filme.get("imDbRating"));
            System.out.println();
        }
        */

        //Desafio_1 Aula_1
        /*
        for (int i = 0; i < 3; i++) {
            Map<String, String> filme = listaDeFilmes.get(i);
            System.out.println(filme.get("title"));
            System.out.println(filme.get("image"));
            System.out.println(filme.get("imDbRating"));
            System.out.println();
        }*/

        //Desafio_2 Aula_1
        /* 
        for (int i = 0; i < 3; i++) {
            Map<String, String> filme = listaDeFilmes.get(i);
            System.out.println("\u001b[1m" + filme.get("title") + "\u001b[0m");
            System.out.println("\u001b[3m" + filme.get("image") + "\u001b[0m");
            //System.out.println(filme.get("imDbRating"));
            System.out.println("\u001b[1m\u001b[37m\u001b[48;2;0;255;133m " + filme.get("imDbRating" ) + " \u001b[0m");

            double classificacao = Double.parseDouble(filme.get("imDbRating")); //Desafio_3 Aula_1
            int numeroEmoji = (int) classificacao;                                  //Desafio_3 Aula_1
            for (int n = 0; n < numeroEmoji; n++) {                                 //Desafio_3 Aula_1
                System.out.print("🎬");                                           //Desafio_3 Aula_1
            }
            System.out.println("\n");
        }
        */

        var geradora = new GeradoraDeFigurinhas();

        /* Original Aula 2
        for (Map<String,String> filme : listaDeFilmes) {
            
            String urlImage =  filme.get("image");
            String titulo = filme.get("title");
            
            InputStream inputStream = new URL(urlImage).openStream();
            String nomeArquivo = titulo + ".png";

            geradora.cria(inputStream, nomeArquivo);

            System.out.println(titulo);
            System.out.println();
        }  
        */ 

        //Desafio_1 Aula_2
        var diretorio = new File("saida/"); 
        diretorio.mkdir();                           
        
        for (int i = 0; i < 5; i++) {
            Map<String, String> filme = listaDeFilmes.get(i);

            String urlImage = filme.get("image");
            String titulo = filme.get("title");
            double classificacao = Double.parseDouble(filme.get("imDbRating"));

            String textoFigurinha;
            InputStream imagemEmoji;
            if (classificacao >= 9.0){
                textoFigurinha = "TOPZERA";
                imagemEmoji = new FileInputStream(new File("sobreposicao/OK.png"));
            } else {
                textoFigurinha = "HUMM...";
                imagemEmoji = new FileInputStream(new File("sobreposicao/BAD.png"));
            }
            
            InputStream inputStream = new URL(urlImage).openStream();
            //String nomeArquivo = "saida/" + titulo + ".png";
            String nomeArquivo = "saida/" + titulo.replace(":", " -") + ".png"; //correção

            geradora.cria(inputStream, nomeArquivo, textoFigurinha, imagemEmoji);

            System.out.println(titulo);
            System.out.println();
        }
    }
}
