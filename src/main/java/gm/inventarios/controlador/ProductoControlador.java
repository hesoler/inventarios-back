package gm.inventarios.controlador;

import gm.inventarios.exception.RecursoNoEncontradoException;
import gm.inventarios.modelo.Producto;
import gm.inventarios.servicio.ProductoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("inventario-app")
@CrossOrigin(value = "http://localhost:4200/")
public class ProductoControlador {

    private static final Logger logger = LoggerFactory.getLogger(ProductoControlador.class);

    @Autowired
    private ProductoServicio productoServicio;

    //http://localhost:8080/inventario-app/productos
    @GetMapping("/productos")
    public List<Producto> obtenerProductos() {
        List<Producto> productos = productoServicio.listarProducto();
        logger.info("Producto obtenidos:");
        productos.forEach(producto -> logger.info(producto.toString()));
        return productos;
//        return this.productoServicio.listarProducto();
    }

    @PostMapping("/productos")
    public Producto crearProducto(@RequestBody Producto producto) {
        logger.info("Crear producto: {}", producto);
        this.productoServicio.guardarProducto(producto);
        return producto;
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Integer id) {
        Producto producto = this.productoServicio.buscarProductoPorId(id);
        if (producto != null) return ResponseEntity.ok(producto);
        else throw new RecursoNoEncontradoException("No se encontró el producto con id:" + id);
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Integer id, @RequestBody Producto productoRecibido) {
        Producto productoActual = this.productoServicio.buscarProductoPorId(id);
        if (productoActual == null)
            throw new RecursoNoEncontradoException("No se encontró el producto con id:" + id);

        productoActual.setDescripcion(productoRecibido.getDescripcion());
        productoActual.setPrecio(productoRecibido.getPrecio());
        productoActual.setExistencia(productoRecibido.getExistencia());
        this.productoServicio.guardarProducto(productoRecibido);
        return ResponseEntity.ok(productoActual);
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Map<String, Boolean>> eliminarProducto(@PathVariable Integer id) {
        Producto producto = this.productoServicio.buscarProductoPorId(id);
        if (producto == null)
            throw new RecursoNoEncontradoException("No se encontró el producto con id:" + id);

        this.productoServicio.eliminarProducto(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("eliminado", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
