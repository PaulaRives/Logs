import java.awt.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class ProcessLogFile {


	static ArrayList<User> listUsers = new ArrayList<User>();

	private static String parsearMoneda(String moneda) {
		String monedaSimbolo;
		switch (moneda) {
		case "32":
		case "1":
		case "032":
		case "PESO":
		case "$":
			monedaSimbolo = "ARP";
			break;
		case "840":
		case "2":
		case "U$S":
		case "DOLAR":
			monedaSimbolo = "USB";
			break;
		default:
			monedaSimbolo = null;
		}
		return monedaSimbolo;
	}

	private static String[] getImporteMoneda(String linea, String verbo) {
		final String dataStartTag = "<data>";
		final String dataEndTag = "</data";
		final String codMonedaStartTag = "<cod_moned>";
		final String codMonedaEndTag = "</cod_moned>";
		final String pagoCodMonedaStartTag = "<pago_moneda_codigo>"; 
		final String pagoCodMonedaEndTag = "</pago_moneda_codigo>"; 
		final String prepagoPesMonedCodigoStartTag = "<prepago_pes_moned_codigo>";
		final String prepagoPesMonedCodigoEndTag = "</prepago_pes_moned_codigo>";
		final String monedaStartTag = "<moneda>";
		final String monedaEndTag = "</moneda>";
		final String cddivisStartTag = "<cddivis>";
		final String cddivisEndTag = "</cddivis>";
		final String importeStartTag = "<importe>";
		final String importeEndTag = "</importe>";
		final String pagoImporteStartTag = "<pago_importe>";
		final String pagoImporteEndTag = "</pago_importe>";
		final String prepagoPesImporteStartTag = "<prepago_pes_importe>";
		final String prepagoPesImporteEndTag = "</prepago_pes_importe>";
		final String impIniStartTag = "<imptini>";
		final String impIniEndTag = "</imptini>";
		final String importeDebStartTag = "<importe_deb>";
		final String importeDebEndTag = "</importe_deb>";
		final String vepImporteStartTag = "<vep_importe>";
		final String vepImporteEndTag = "</vep_importe>";
		final String vepMonedaCodigoStartTag = "<vep_moneda_codigo>";
		final String vepMonedaCodigoEndTag = "</vep_moneda_codigo>";		
		
		int startIndex;
		int endIndex;

		int startDataIndex = linea.indexOf(dataStartTag);
		int endDataIndex = linea.indexOf(dataEndTag);
		if (startDataIndex == -1 || endDataIndex == -1) {
			System.out.println("Algo no esta bien 1");
		}
		String tagData = linea.substring(startDataIndex, endDataIndex);

		String moneda = null;
		String importe = null;
		switch (verbo) {
		case Verbos.TransferenciaCuentasPropias:
		case Verbos.TransferenciaTerceros:
		case Verbos.AltaTam:
		case Verbos.LiberarTam:	
			// importe
			startIndex = tagData.indexOf(importeStartTag);
			endIndex = tagData.indexOf(importeEndTag);
			importe = tagData.substring(startIndex + importeStartTag.length(), endIndex);
			// moneda
			startIndex = tagData.indexOf(codMonedaStartTag);
			endIndex = tagData.indexOf(codMonedaEndTag);
			moneda = tagData.substring(startIndex + codMonedaStartTag.length(), endIndex);
			break;
		case Verbos.PafoServiciosFactura:
		case Verbos.PagoServiciosTarjeta:
		case Verbos.PagoServiciosCelular:
			// importe
			startIndex = tagData.indexOf(pagoImporteStartTag);
			endIndex = tagData.indexOf(pagoImporteEndTag);
			boolean isPagoImporte = tagData.indexOf(pagoImporteStartTag) != -1 && tagData.indexOf(pagoImporteEndTag) != -1;
			boolean isPrepagoPesImporte = tagData.indexOf(prepagoPesImporteStartTag) != -1 && tagData.indexOf(prepagoPesImporteEndTag) != -1;
		    boolean isVepImporte = tagData.indexOf(vepImporteStartTag) != -1 && tagData.indexOf(vepImporteEndTag) != -1;
			
		    if (isPagoImporte) {
		    	startIndex = tagData.indexOf(pagoImporteStartTag);
				endIndex = tagData.indexOf(pagoImporteEndTag);
		    	importe = tagData.substring(startIndex + pagoImporteStartTag.length(), endIndex);	
		    }
		    if (isPrepagoPesImporte) {
		    	startIndex = tagData.indexOf(prepagoPesImporteStartTag);
				endIndex = tagData.indexOf(prepagoPesImporteEndTag);
		    	importe = tagData.substring(startIndex + prepagoPesImporteStartTag.length(), endIndex);
		    }
		    if (isVepImporte) {
		    	startIndex = tagData.indexOf(vepImporteStartTag);
				endIndex = tagData.indexOf(vepImporteEndTag);
		    	importe = tagData.substring(startIndex + vepImporteStartTag.length(), endIndex);
		    }
		    
		    boolean isPagoCodMoneda = tagData.indexOf(pagoCodMonedaStartTag) != -1 && tagData.indexOf(pagoCodMonedaEndTag) != -1;
			boolean isPrepagoPesCodMoneda = tagData.indexOf(prepagoPesMonedCodigoStartTag) != -1 && tagData.indexOf(prepagoPesMonedCodigoEndTag) != -1;
			boolean isVepMonedaCodigo = tagData.indexOf(vepMonedaCodigoStartTag) != -1 && tagData.indexOf(vepMonedaCodigoEndTag) != -1;
			
			if (isPagoCodMoneda) {
				startIndex = tagData.indexOf(pagoCodMonedaStartTag);
				endIndex = tagData.indexOf(pagoCodMonedaEndTag);
				moneda = tagData.substring(startIndex + pagoCodMonedaStartTag.length(), endIndex);
			}
			if (isPrepagoPesCodMoneda) {
				startIndex = tagData.indexOf(prepagoPesMonedCodigoStartTag);
				endIndex = tagData.indexOf(prepagoPesMonedCodigoEndTag);
				moneda = tagData.substring(startIndex + prepagoPesMonedCodigoStartTag.length(), endIndex);
			}
			if(isVepMonedaCodigo) {
				startIndex = tagData.indexOf(vepMonedaCodigoStartTag);
				endIndex = tagData.indexOf(vepMonedaCodigoEndTag);
				moneda = tagData.substring(startIndex + vepMonedaCodigoStartTag.length(), endIndex);
			}
			break;
		case Verbos.AltaPlazoFijo:
		case Verbos.RenovarPlazoFijo:
			// importe
			startIndex = tagData.indexOf(impIniStartTag);
			endIndex = tagData.indexOf(impIniEndTag);
			
			if(startIndex == -1 || endIndex == -1) {
				System.out.println("a");
				importe = tagData.substring(startIndex + impIniStartTag.length(), endIndex);
			}
				
			// moneda
			startIndex = tagData.indexOf(monedaStartTag);
			endIndex = tagData.indexOf(monedaEndTag);
			moneda = tagData.substring(startIndex + monedaStartTag.length(), endIndex);
			break;
		case Verbos.CompraDivisa:
		case Verbos.VentaDivisa:
			// importe
			startIndex = tagData.indexOf(importeDebStartTag);
			endIndex = tagData.indexOf(importeDebEndTag);
			importe = tagData.substring(startIndex + importeDebStartTag.length(), endIndex);
			// moneda
			startIndex = tagData.indexOf(cddivisStartTag);
			endIndex = tagData.indexOf(cddivisEndTag);
			moneda = tagData.substring(startIndex + cddivisStartTag.length(), endIndex);
			break;
		default:
			importe = "";
			moneda = "";

		}

		if (moneda != null && !moneda.equals("ARP") && !moneda.equals("USB"))
			moneda = parsearMoneda(moneda);

		return new String[] { importe, moneda };

	}

	private static String getJsonImporteMoneda(String[] impMoneda) {
		return "\"COD_MONEDA\":\"" + impMoneda[1] + "\"," + "\"IMPORTE\":\"" + impMoneda[0] + "\"";
	}

	private static String getVerbo(String linea) {
		String[] lineaOp = linea.split(" ");
		if(lineaOp.length >= 5) {
		 
		 try {
			String[] usuarioYVerbo = lineaOp[5].split("\\["); 
			 return usuarioYVerbo.length >= 3 ? usuarioYVerbo[3].split("\\]")[0] : "";
		 }
		 catch(Exception e){
			 System.out.println("ACA SE ROMPE:" + linea);
		 }
		 
		
		}
		return "";
	}
	
	private static String[] getData(String linea) {
		
		// buscar documento
		final String dataStartTag = "<data>";
		final String dataEndTag = "</data";
		int startDataIndex = linea.indexOf(dataStartTag);
		int endDataIndex = linea.indexOf(dataEndTag);
		
		/*if (startDataIndex != -1 && endDataIndex != -1) {
			String tagData = linea.substring(startDataIndex, endDataIndex);
			String usuarioStartTag = "<usuario>";
			String usuarioEndTag = "</usuario>";
			String usuario;
			int startIndex = tagData.indexOf(usuarioStartTag);
			int endIndex = tagData.indexOf(usuarioEndTag);
			if (startIndex != -1 && endIndex != -1)
				usuario = tagData.substring(startIndex +usuarioStartTag.length(), endIndex);
			/*if(usuario != null) {
				
			}*/
		//}*/
		
		
		String[] lineaOp = linea.split(" ");
		String verbo = getVerbo(linea);
		
		return new String[] { lineaOp[0], lineaOp[1], verbo, /*sessionid*/lineaOp[3] };
	}

	private static String getJsonDataTexto(String[] info) {
		return ("{\"fechaDiaHora\":\"" + info[0] + " "  + info[1] + "\"," +  "\"idSession\":\"" + info[3] + "\"," + "\"verbo\":\"" + info[2]  + "\"");
	}

	public static void Write(String pathToRead) {
		FileReader fr = null;
		FileWriter fw = null;
		BufferedReader br = null;

		try {
			// salida json (el archivo tiene que existir)
			fw = new FileWriter("result1.txt", true);
			//FileWriter fw2 = new FileWriter("errores.txt");
			// path del log
			fr = new FileReader(pathToRead);
			br = new BufferedReader(fr);

			String linea;
			
			while ((linea = br.readLine()) != null) {
				if (!linea.contains("[FALTA CONFIGURAR]") && !linea.contains("[INFO]") && !linea.contains("MBR.MBRPKG_LIB") 
						&& !linea.contains("<resultado>fallo</resultado>")) {
					// obtiene informacion de la linea
					String verbo = getVerbo(linea);
					if(Verbos.Verbos.contains(verbo)) {
						/*if (linea.contains("[Login]startUpHtmlSessionHWOp.HttpSendAndRecSrv]resp")){
							// buscar documento
							final String dataStartTag = "<data>";
							final String dataEndTag = "</data";
							int startDataIndex = linea.indexOf(dataStartTag);
							int endDataIndex = linea.indexOf(dataEndTag);
							if (startDataIndex != -1 && endDataIndex != -1) {
								String tagData = linea.substring(startDataIndex, endDataIndex);
								String usuarioStartTag = "<usuario>";
								String usuarioEndTag = "</usuario>";
								String dniStartTag = "<num_doc>";
								String dniEndTag = "</num_doc>";
								String usuario = null;
								int startIndex = tagData.indexOf(usuarioStartTag);
								int endIndex = tagData.indexOf(usuarioEndTag);
								if (startIndex != -1 && endIndex != -1)
									usuario = tagData.substring(startIndex +usuarioStartTag.length(), endIndex);
								startIndex = tagData.indexOf(dniStartTag);
								endIndex = tagData.indexOf(dniEndTag);
								String doc = null;
								if (startIndex != -1 && endIndex != -1)
								doc = tagData.substring(startIndex + dniStartTag.length(), endIndex);
								listUsers.add(new User(usuario, doc));
							}
							
						}*/
						if (linea.contains("[Transferencia_Cuentas_Propias]NewTransferOwnOp.HttpSendAndRecSrv]resp")
								|| linea.contains("[Transferencia_Terceros]NewTransferOutOp.HttpSendAndRecSrv2]resp")
								|| linea.contains("[Pago_Servicios_Factura]NewPaymentBillOp.HttpSendAndRecSrv]resp")
								|| linea.contains("[Pago_Servicios_Tarjeta]NewPaymentCardOp.HttpSendAndRecSrv]resp")
								|| linea.contains("[Pago_Servicios_Celular]NewPaymentCellPhoneOp.HttpSendAndRecSrv1]resp")
								|| linea.contains("[Alta_Plazo_Fijo]NewImpositionOp.HttpSendAndRecSrv1]resp")
								|| linea.contains("[Renovar_Plazo_Fijo]RenovateImpositionOp.HttpSendAndRecSrv]resp")
								|| linea.contains("[Compra_Divisa]ConfirmDollarTransferBuyOp.HttpSendAndRecSrv]resp")
								|| linea.contains("[Venta_Divisa]ConfirmDollarTransferSellOp.HttpSendAndRecSrv]resp")) {
							String[] info = getData(linea);
							String[] impMoneda = getImporteMoneda(linea, info[2]);
							String infoJson = getJsonDataTexto(info);
							String importeMonedaJson = getJsonImporteMoneda(impMoneda);
							if (infoJson != null && importeMonedaJson != null) {
								fw.write(infoJson + "," + importeMonedaJson + "}" + System.getProperty("line.separator"));
							}
						} else {
							if (linea.contains(".HWSetReplyPage") && !Verbos.VerbosConImporte.contains(verbo)) {
								String[] info = getData(linea);
								String infoJson = getJsonDataTexto(info);
								if (infoJson != null) {
									fw.write(infoJson + "}" + System.getProperty("line.separator"));
								}
							}
						}
					}
				}
			}

			System.out.println("Log Procesado: " + Paths.get(pathToRead).getFileName() + "Salida en results.txt");
			fr.close();
			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	}
}