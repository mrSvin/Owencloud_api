import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;

class main {
    public static String token = "8H6cCvzElwm8cymIl79BWUz3DgUMdukT";
    public static String[] Array = new String[20];;
    public static String content = " ";
    public static String[] name_Array = {"Вес на выгрузке, кг","индикация авария","индикация готов","индикация работа","каретка лево","каретка право","каретка центр лево2",
            "каретка центр право 2","количество выполненных операций всего","нахождение в базе",
            "номер введенной ячейки","последняя ошибка","ПЧВ 0.75 кВт Мощность","ПЧВ 0.75 кВт Частота","ПЧВ 7.5 кВт Мощность","ПЧВ 7.5 кВт Частота","Статус выполнения команды в базу"
            ,"Статус выполнения команды Вернуть","статус выполнения команды Взять","счетчик флагов",};

    public static void main(String []args) throws IOException {
        api_owencloud_token();    //получаем токен
        api_owencloud_sklad();      //берем данные со склада
    }

    //Для получения токена
    public static void api_owencloud_token() throws IOException{
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "{\r\n\t\"login\":\"alva@sespel.com\",\r\n\t\"password\":\"alex9231\"\r\n}");
        Request request = new Request.Builder()
                .url("https://api.owencloud.ru/v1/auth/open")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = client.newCall(request).execute();
        //System.out.println(response.body().string());
        JSONObject jsonResponse = new JSONObject(response.body().string());
        token = jsonResponse.getString("token");
        System.out.println("Полученный токен: " + token);
    }

    //Для получения инфоромации о оборудовании
    public static void api_owencloud_sklad() throws IOException{
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://api.owencloud.ru/v1/device/165257")
                .method("GET", null)
                .addHeader("Authorization", "Bearer "+ token )
                .build();
        Response response = client.newCall(request).execute();
        //System.out.println(response.body().string());

        //Получаяем всё сообщение запроса
        JSONObject jsonResponse = new JSONObject(response.body().string());
        //System.out.println(jsonResponse);
        //String parameters = jsonResponse.getString("name");
        //System.out.println(parameters);

        //Считываем массив с параметрами оборудования
        JSONArray parameters = jsonResponse.getJSONArray("parameters");
        //System.out.println(parameters);

        //Выдергиваем интересующие нас значения регистров
        int n = parameters.length();
        for (int i = 0; i < n; ++i) {
            final JSONObject person = parameters.getJSONObject(i);
            //System.out.println(person.getString("value"));
            Array[i]=person.getString("value");
            //System.out.println("массив" + i + ": "+ Array[i]);

            content =content  + "\n" + name_Array[i] + ": " + Array[i];
        }

        String path = "D:\\java\\projects\\Owencloud_api\\output.txt";
        Files.write( Paths.get(path), content.getBytes());
        System.out.println("Операция записи данных в output.txt выполнена!");

    }


}
