package dds.monedero.model;

import java.math.BigDecimal;

public interface TipoOperacion {
    public BigDecimal calcularNuevoSaldo(BigDecimal saldo, BigDecimal monto);
    public boolean isDeposito();
}
