def registro_pedido(pedidos):
    nombre = input("ingrese su nombre y apellido:")
    direccion = input("ingrese su direccion:")
    sector = input("ingrese el sector donde vive(centro, norte, sur)")
    paq_pequeño = int(input("¿cuantos pedidos pequeños lleva?"))
    paq_mediano = int(input("¿cuantos pedidos medianos lleva?"))
    paq_grande = int(input("¿cuantos pedidos grandes lleva?"))
    ({
    'nombre':nombre,
    'direccion': direccion,
    'sector': sector,
    'cantidad de pedidos pequeños': paq_pequeño,
    'cantidad de pedidos pequeños': paq_mediano,
    'cantidad de pedidos pequeños': paq_grande
    })
registro_pedido()
def donde():
    lugar = input("ingrese el sector de la planilla")
    if input == "sur":
        for sector in datos(sector):
            print(sector)