var app = angular.module('personagemApp', []);
app.controller('PessoaController', function ($scope, $http, $location) {
    
    $scope.urlAtual = $location.absUrl();
    
    $scope.salvar = function () {
        console.log($scope.fields);
        $http({
            method: 'POST',
            data: $scope.fields,
            url: $scope.urlAtual + 'api/pessoas',
            headers: {'Content-Type': 'application/json'}
        }).success(function (data, status, headers, config) {
            console.log(data);
            $scope.todos();
        }).error(function (data, status, headers, config) {
            console.log(data);
            $scope.todos();
        });
    };
        
    $scope.todos = function() {
        $http.get($scope.urlAtual + 'api/pessoas').success(function (data) {
            $scope.pessoas = data;
            $scope.existemDados = true;
        });
    };

    $scope.consultar = function(pessoa) {
        $http.get($scope.urlAtual + 'api/pessoas/' + pessoa.id).success(function (data) {
            console.log(data);
            $scope.fields = data;
        });
    };

    $scope.excluir = function(pessoa) {
        $http.delete($scope.urlAtual + 'api/pessoas/' + pessoa.id).success(function (data) {
            console.log(data);
            $scope.fields = data;
            $scope.todos();
        });
    };
    
});
