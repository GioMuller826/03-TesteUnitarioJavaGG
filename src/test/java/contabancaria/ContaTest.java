package contabancaria;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ContaTest {

    // =============================
    // TESTES DO CONSTRUTOR
    // =============================

    @Test
    void construtor_DadosValidos_CriaContaCorretamente() {
        var conta = new Conta("Maria", 100);

        assertEquals("Maria", conta.getTitular());
        assertEquals(100, conta.getSaldo());
        assertTrue(conta.isAtiva());
    }

    @Test
    void construtor_SemSaldoInicial_CriaContaComSaldoZero() {
        var conta = new Conta("João");

        assertEquals("João", conta.getTitular());
        assertEquals(0, conta.getSaldo());
        assertTrue(conta.isAtiva());
    }

    @Test
    void construtor_TitularNulo_LancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Conta(null));
    }

    @Test
    void construtor_TitularVazio_LancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Conta(""));
    }

    @Test
    void construtor_SaldoNegativo_LancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Conta("Maria", -50));
    }

    @ParameterizedTest
    @CsvSource({
            "Ana, 0",
            "Carlos, 1000",
            "Beatriz, 0.01"
    })
    void construtor_VariosValoresValidos_CriaContaCorretamente(String titular, double saldo) {
        var conta = new Conta(titular, saldo);

        assertEquals(titular, conta.getTitular());
        assertEquals(saldo, conta.getSaldo(), 0.001);
        assertTrue(conta.isAtiva());
    }

    // =============================
    // TESTES DE DEPOSITAR
    // =============================

    @Test
    void depositar_ValorValido_AtualizaSaldo() {
        var conta = new Conta("Maria", 100);

        conta.depositar(50);

        assertEquals(150, conta.getSaldo());
    }

    @Test
    void depositar_ValorZero_LancaIllegalArgumentException() {
        var conta = new Conta("Maria", 100);

        assertThrows(IllegalArgumentException.class, () -> conta.depositar(0));
    }

    @Test
    void depositar_ValorNegativo_LancaIllegalArgumentException() {
        var conta = new Conta("Maria", 100);

        assertThrows(IllegalArgumentException.class, () -> conta.depositar(-10));
    }

    @Test
    void depositar_ContaInativa_LancaIllegalStateException() {
        var conta = new Conta("Maria", 0);
        conta.encerrar();

        assertThrows(IllegalStateException.class, () -> conta.depositar(10));
    }

    // =============================
    // TESTES DE SACAR
    // =============================

    @Test
    void sacar_ValorValido_AtualizaSaldo() {
        var conta = new Conta("Maria", 100);

        conta.sacar(50);

        assertEquals(50, conta.getSaldo());
    }

    @Test
    void sacar_ValorMaiorQueSaldo_LancaIllegalStateException() {
        var conta = new Conta("Maria", 100);

        assertThrows(IllegalStateException.class, () -> conta.sacar(200));
    }

    @Test
    void sacar_ValorZero_LancaIllegalArgumentException() {
        var conta = new Conta("Maria", 100);

        assertThrows(IllegalArgumentException.class, () -> conta.sacar(0));
    }

    @Test
    void sacar_ValorNegativo_LancaIllegalArgumentException() {
        var conta = new Conta("Maria", 100);

        assertThrows(IllegalArgumentException.class, () -> conta.sacar(-10));
    }

    @Test
    void sacar_ContaInativa_LancaIllegalStateException() {
        var conta = new Conta("Maria", 0);
        conta.encerrar();

        assertThrows(IllegalStateException.class, () -> conta.sacar(10));
    }

    // =============================
    // TESTES DE TRANSFERIR
    // =============================

    @Test
    void transferir_ValorValido_AtualizaSaldoDeAmbasContas() {
        var origem = new Conta("Maria", 200);
        var destino = new Conta("João", 100);

        origem.transferir(destino, 50);

        assertEquals(150, origem.getSaldo());
        assertEquals(150, destino.getSaldo());
    }

    @Test
    void transferir_SaldoInsuficiente_LancaIllegalStateException() {
        var origem = new Conta("Maria", 100);
        var destino = new Conta("João", 100);

        assertThrows(IllegalStateException.class, () -> origem.transferir(destino, 200));
    }

    @Test
    void transferir_ValorZero_LancaIllegalArgumentException() {
        var origem = new Conta("Maria", 100);
        var destino = new Conta("João", 100);

        assertThrows(IllegalArgumentException.class, () -> origem.transferir(destino, 0));
    }

    @Test
    void transferir_ContaOrigemInativa_LancaIllegalStateException() {
        var origem = new Conta("Maria", 0);
        var destino = new Conta("João", 100);

        origem.encerrar();

        assertThrows(IllegalStateException.class, () -> origem.transferir(destino, 50));
    }

    @Test
    void transferir_ContaDestinoInativa_LancaIllegalStateException() {
        var origem = new Conta("Maria", 100);
        var destino = new Conta("João", 0);

        destino.encerrar();

        assertThrows(IllegalStateException.class, () -> origem.transferir(destino, 50));
    }

    // =============================
    // TESTES DE ENCERRAR
    // =============================

    @Test
    void encerrar_ContaComSaldoZero_Funciona() {
        var conta = new Conta("Maria", 0);

        conta.encerrar();

        assertFalse(conta.isAtiva());
    }

    @Test
    void encerrar_ContaComSaldo_LancaIllegalStateException() {
        var conta = new Conta("Maria", 100);

        assertThrows(IllegalStateException.class, () -> conta.encerrar());
    }

    @Test
    void encerrar_ContaJaInativa_LancaIllegalStateException() {
        var conta = new Conta("Maria", 0);

        conta.encerrar();

        assertThrows(IllegalStateException.class, () -> conta.encerrar());
    }
}