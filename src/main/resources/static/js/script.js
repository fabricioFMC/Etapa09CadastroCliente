function confirmarExclusao(buttonElement) {
    // Pega os dados do próprio botão clicado
    const id = buttonElement.getAttribute('data-id');
    const nome = buttonElement.getAttribute('data-nome');
    
    if(confirm("Tem certeza que deseja excluir o cliente: " + nome + "?")) {
        window.location.href = "/deletar/" + id;
    }
}

$(document).ready(function(){
    // Máscara para CPF: 000.000.000-00
    $('input[name="cpf"]').mask('000.000.000-00', {reverse: true});
    
    // Máscara para Telefone: (00) 00000-0000
    $('input[name="telefone"]').mask('(00) 00000-0000');
});
console.log("Sistema carregado com sucesso!");