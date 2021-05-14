package dds.monedero.model;

import java.time.LocalDate;

public class Movimiento {
  private LocalDate fecha;
  //En ningún lenguaje de programación usen jamás doubles para modelar dinero en el mundo real
  //siempre usen numeros de precision arbitraria, como BigDecimal en Java y similares
  private double monto;
  private TipoOperacion tipoOperacion;

  public Movimiento(LocalDate fecha, double monto, TipoOperacion tipoOperacion) {
    this.fecha = fecha;
    this.monto = monto;
    this.tipoOperacion = tipoOperacion;
  }

  public double getMonto() {
    return monto;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public boolean fueDepositado(LocalDate fecha) {
    return isDeposito() && esDeLaFecha(fecha);
  }

  public boolean fueExtraido(LocalDate fecha) {
    return !isDeposito() && esDeLaFecha(fecha);
  }

  public boolean esDeLaFecha(LocalDate fecha) {
    return this.fecha.equals(fecha);
  }

  public boolean isDeposito() {
    return tipoOperacion.isDeposito();
  }

  public double calcularValor(double saldo, double monto) {
    return this.tipoOperacion.calcularNuevoSaldo(saldo,monto);
  }

}
