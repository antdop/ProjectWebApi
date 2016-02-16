//function success(position) {
//    var s = document.querySelector('#status');
//
//    if (s.className == 'success') {
//        return;
//    }
//
//    s.innerHTML = "Você foi localizado!";
//    s.className = 'success';
//
//    var mapcanvas = document.createElement('div');
//    mapcanvas.id = 'mapcanvas';
//    mapcanvas.style.height = '250px';
//    mapcanvas.style.width = '300px';
//
//    document.querySelector('article').appendChild(mapcanvas);
//
////    var latlng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
//
//    var latlng = marker.getposition();
//
//    var myOptions = {
//        zoom: 15,
//        center: latlng,
//        mapTypeControl: false,
//        navigationControlOptions: {style: google.maps.NavigationControlStyle.SMALL},
//        mapTypeId: google.maps.MapTypeId.ROADMAP
//    };
//
//    var map = new google.maps.Map(document.getElementById("mapcanvas"), myOptions);
//    var marker = new google.maps.Marker({
//        position: latlng,
//        map: map,
//        title: "Você está aqui!"
//    });
//
//}
//
//function error(msg) {
//    var s = document.querySelector('#status');
//    s.innerHTML = typeof msg == 'string' ? msg : "falhou";
//    s.className = 'fail';
//}
//
//if (navigator.geolocation) {
//    navigator.geolocation.getCurrentPosition(success, error);
//} else {
//    error('Seu navegador não suporta <b style="color:black;background-color:#ffff66">Geolocalização</b>!');
//}

function limpa_formulário_cep() {
    //Limpa valores do formulário de cep.
    document.getElementById('logradouro').value = ("");
    document.getElementById('bairro').value = ("");
    document.getElementById('localidade').value = ("");
    document.getElementById('uf').value = ("");
    document.getElementById('ibge').value = ("");
}

function meu_callback(conteudo) {
    if (!("erro" in conteudo)) {
        //Atualiza os campos com os valores.
        document.getElementById('logradouro').value = (conteudo.logradouro);
        document.getElementById('bairro').value = (conteudo.bairro);
        document.getElementById('localidade').value = (conteudo.localidade);
        document.getElementById('uf').value = (conteudo.uf);
        document.getElementById('ibge').value = (conteudo.ibge);
    } //end if.
    else {
        //CEP não Encontrado.
        limpa_formulário_cep();
        alert("CEP não encontrado.");
    }
}

function pesquisacep(valor) {

    //Nova variável "cep" somente com dígitos.
    var cep = valor.replace(/\D/g, '');

    //Verifica se campo cep possui valor informado.
    if (cep != "") {

        //Expressão regular para validar o CEP.
        var validacep = /^[0-9]{8}$/;

        //Valida o formato do CEP.
        if (validacep.test(cep)) {

            //Preenche os campos com "..." enquanto consulta webservice.
            document.getElementById('logradouro').value = "...";
            document.getElementById('bairro').value = "...";
            document.getElementById('localidade').value = "...";
            document.getElementById('uf').value = "...";
            document.getElementById('ibge').value = "...";

            //Cria um elemento javascript.
            var script = document.createElement('script');

            //Sincroniza com o callback.
            script.src = '//viacep.com.br/ws/' + cep + '/json/?callback=meu_callback';

            //Insere script no documento e carrega o conteúdo.
            document.body.appendChild(script);

        } //end if.
        else {
            //cep é inválido.
            limpa_formulário_cep();
            alert("Formato de CEP inválido.");
        }
    } //end if.
    else {
        //cep sem valor, limpa formulário.
        limpa_formulário_cep();
    }
}
;
