package util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class InputManagerTest {
    private final InputStream originalSystemIn = System.in;

    @AfterEach
    void restoreStreams() {
        System.setIn(originalSystemIn);
    }

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    // ========== TESTES DE lerString ==========
    
    @Test
    void lerString_semEntradaDisponivel_lancaRuntimeException() {
        provideInput("");
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            InputManager.lerString("Digite algo");
        });
        
        assertEquals("Entrada não disponível.", exception.getMessage());
    }

    // ========== TESTES DE lerSimNao ==========

    @Test
    void lerSimNao_entradaS_retornaTrue() {
        provideInput("s\n");
        
        boolean resultado = InputManager.lerSimNao("Deseja continuar?");
        
        assertTrue(resultado);
    }

    @Test
    void lerSimNao_semEntradaDisponivel_lancaException() {
        provideInput("");
        
        assertThrows(NoSuchElementException.class, () -> {
            InputManager.lerSimNao("Deseja continuar?");
        });
    }
}