package Menu;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import Usuario.Usuario;



public class Menu {
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		
		
		
		String password1 = hashPassword("password");
		String password2 = hashPassword("123456");
		String password3 = hashPassword("gatocan");

		Usuario user1 = new Usuario("paul", password1);
		Usuario user2 = new Usuario("gonzalo", password2);
		Usuario user3 = new Usuario("sandra", password3);
		
		List<Usuario> usuarios = new ArrayList<Usuario>();
		usuarios.add(user1);
		usuarios.add(user2);
		usuarios.add(user3);
				
		
		// falta hacer que si los intentos son > 3 se cierre la aplicacion
		
		
		int intentos = 0;
		
		do {
			// Pedimos por consola que se identifique
			Usuario logged = logIn();
			
			// Comprobamos que el usuario es correcto, sino, no continúa el menú y nos lo vuelve a preguntar hasta 2 veces más
			for (Usuario usuario : usuarios) {
				
				if (logged.equals(usuario)) {
					System.out.println("\n login correcto, bienvenido " + usuario.getNombre() + "\n");
				
				
					try {
			
			// Primero creamos todos los objetos necesarios para el cifrado sim�trico:
			// Creamos el generador de claves, elegimos el algoritmo AES		
			KeyGenerator generadorSimetrica = KeyGenerator.getInstance("AES");
			
			// Creamos la clave sim�trica con la que vamos a encriptar:
			SecretKey claveSecreta = generadorSimetrica.generateKey();
			
			// Creamos el objeto que va a cifrar y descifrar las frases y objetos con la clave, 
			// le pasamos el algoritmo que usa para cifrar, que ser� el AES
			Cipher cifradorSimetrico = Cipher.getInstance("AES");
			
			
			// Lo iniciamos o configuramos para encriptar:
			cifradorSimetrico.init(Cipher.ENCRYPT_MODE, claveSecreta);
			
			// Creamos un objeto Cipher para desencriptado sim�trico:
			Cipher descifradorSimetrico = Cipher.getInstance("AES");
			
			// Lo iniciamos o configuramos para desencriptar:
			descifradorSimetrico.init(Cipher.DECRYPT_MODE, claveSecreta);
			
			// Creamos la variable que almacena la frase original y la cifrada
			String frase = null;
			String fraseCifrada = null;
			
			// Creamos una variable boolean para controlar la repetici�n del bucle.			
			boolean continuar = true;
			
			do {
				// Creamos el man� para que salga por pantalla y permita escoger una opci�n 
				System.out.println("ELIJA UNA OPCI�N:\n\n"
						+ "  0. Salir \n"
						+ "  1. Encriptar frase \n"
						+ "  2. Mostrar frase encriptada \n"
						+ "  3. Desencriptar frase \n"
						);

				// Creamos un objeto Scanner para pedir por pantalla la opcion del men� deseada
				Scanner sc = new Scanner(System.in);
				int opcion = sc.nextInt();	

				switch (opcion) {
					case 0:
						System.out.println("----SALIR----");
						System.out.println("La aplicaci�n ha terminado ");
						continuar = false;
						break;
					case 1:
						System.out.println("----ENCRIPTAR FRASE----");
						// Pedimos la frase por consola y la almacenamos en una variable de tipo String:
						System.out.println("Escriba la frase que desea encriptar:");
						Scanner scanner = new Scanner(System.in);
						frase = scanner.nextLine();
						// Convertimos el String recibido en un array o flujo de bytes para poder cifrar la frase
						byte[] bytesFrase = frase.getBytes();
						byte[] bytesFraseCifrada = cifradorSimetrico.doFinal(bytesFrase);
						// Guardamos en la variable tipo String fraseCifrada el valor del flujo de bytes de la frase cifrada pasado a String
						fraseCifrada = new String(bytesFraseCifrada);
						System.out.println("La frase ha sido cifrada con cifrado sim�trico");
						break;
					case 2:
						System.out.println("----MOSTRAR FRASE ENCRIPTADA----");
						// Mostramos la frase original:
						System.out.println("La frase original era: " + frase);
						// Mostramos por pantalla la frase encriptada almacenada en memoria
						System.out.println("La frase encriptada es la siguiente: " + fraseCifrada);
						break;
					case 3:
						System.out.println("----DESCIFRAR FRASE----");
						// Guardamos en un flujo de bytes la frase desencriptada:
						byte[] bytesFraseDescifrada = descifradorSimetrico.doFinal(fraseCifrada.getBytes());
						// Mostramos la frase descifrada por pantalla, pas�ndola a String
						System.out.println("La frase desencriptada es: " + new String(bytesFraseDescifrada));
						break;
				}
				
			}	while (continuar); 
			
		} catch (Exception e) {
			System.out.println("Un error ha ocurrido... " + e.getMessage());
			e.printStackTrace();
		} 
				}
			} //Termina el menú
			
			// Si nunca ha entrado por usuario correcto, sale este mensajes, se incrementa el numero de intentos,
			// y acaba dando el mensaje final cuando intentos = 3. Pero, si entra por usuario, 
			// cuando termina, también sale este mensajes y vuelve a sumar un intento, y tras entrar tres veces acabas
			//con los mismos mensajes. 
				
			System.out.println("\n Usuario incorrecto \n");
				intentos++;
				
			if (intentos == 3) {
						System.out.println("\n No tienes más intentos \n");
					}		
			
		} while (intentos <3 );
		
	}

	public static String hashPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(password.getBytes());
		String mensajeHashBase64 = Base64.getEncoder().encodeToString(md.digest());
		return mensajeHashBase64;
	} 
		
		public static Usuario logIn() throws NoSuchAlgorithmException {
		System.out.println("Introduzca su usuario: \n");
		Scanner scanner = new Scanner(System.in);
		String nombre = scanner.nextLine();
		
		System.out.println("Introduzca su password: \n");
		String password = hashPassword(scanner.nextLine());
		
		Usuario usuario = new Usuario(nombre, password);
		return usuario;		
		}

	}
