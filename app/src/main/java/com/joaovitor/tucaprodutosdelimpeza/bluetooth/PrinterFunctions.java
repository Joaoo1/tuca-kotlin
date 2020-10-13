package com.joaovitor.tucaprodutosdelimpeza.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.widget.Toast;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joaovitor.tucaprodutosdelimpeza.data.model.Sale;
import com.joaovitor.tucaprodutosdelimpeza.data.model.ProductSale;
import com.joaovitor.tucaprodutosdelimpeza.data.model.Client;
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore;
import com.joaovitor.tucaprodutosdelimpeza.util.FormatDate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;

public class PrinterFunctions {

    private Context mContext;
    private BluetoothPrinter mPrinter;
    private final BluetoothAdapter btAdapter;
    private CollectionReference clientsRef;

    public PrinterFunctions(Context context){
        mContext = context;
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        clientsRef = FirebaseFirestore.getInstance().collection(Firestore.COL_CLIENTS);
    }

    public BluetoothAdapter getBtAdapter(){
        return btAdapter;
    }

    public void printReceipt(final Sale sale, final List<ProductSale> products){
        try{
            BluetoothDevice mBtDevice = btAdapter.getBondedDevices().iterator().next();
            mPrinter = new BluetoothPrinter(mBtDevice);
            mPrinter.connectPrinter(new BluetoothPrinter.PrinterConnectListener(){
                @Override
                public void onConnected() {
                    if(products != null && !products.isEmpty()){
                        mPrinter.setAlign(BluetoothPrinter.ALIGN_CENTER);
                        mPrinter.setBold(true);
                        mPrinter.setBigSize();
                        mPrinter.printText("COMERCIAL MKM");
                        mPrinter.setNormal();
                        mPrinter.printText("Produtos de limpeza");
                        mPrinter.setBold(false);
                        mPrinter.setAlign(BluetoothPrinter.ALIGN_LEFT);
                        mPrinter.printText("CNPJ:11.884.184/0001-16");
                        mPrinter.printText("R. 31 de Marco, Herval D'Oeste");
                        mPrinter.printText("Fone/Whatsapp: (49)99965-6732");
                        mPrinter.printLine();
                        mPrinter.setSmallSize();
                        mPrinter.printText(FormatDate.INSTANCE.formatDateToString(sale.getSaleDate())
                                +"         COD. VENDA:"+String.format(new Locale("pt","BR"),"%05d",sale.getSaleId()));
                        mPrinter.printText("Cliente: "+sale.getClientName());
                        mPrinter.addNewLine();
                        mPrinter.setAlign(BluetoothPrinter.ALIGN_CENTER);
                        mPrinter.setBigSize();
                        mPrinter.setBold(true);
                        mPrinter.printText("RECIBO");
                        mPrinter.addNewLine();
                        mPrinter.setAlign(BluetoothPrinter.ALIGN_LEFT);
                        mPrinter.setUnderline();
                        mPrinter.printText("Produto                    Preco");
                        mPrinter.setNormal();
                        mPrinter.addNewLine();
                        for(int i=0;i<products.size();i++) {
                            mPrinter.setAlign(BluetoothPrinter.ALIGN_LEFT);
                            mPrinter.setNormal();
                            mPrinter.printText(" " + products.get(i).getName());
                            mPrinter.setSmallSize();
                            mPrinter.setAlign(BluetoothPrinter.ALIGN_RIGHT);
                            mPrinter.printText(products.get(i).getQuantity() +

                                "x R$"+products.get(i).getPrice()+"              R$" + calculateTotalProduct(products.get(i).getPrice(), products.get(i).getQuantity()));
                        }
                        mPrinter.setNormal();
                        mPrinter.printLine();
                        mPrinter.setAlign(BluetoothPrinter.ALIGN_RIGHT);
                        mPrinter.printText("Sub Total  R$"+sale.getGrossValue());
                        if(!sale.getPaidValue().isEmpty()) {
                          BigDecimal amountPaid = new BigDecimal(sale.getPaidValue());
                          if(amountPaid.compareTo(new BigDecimal("0.00")) > 0){
                            mPrinter.printText("Valor pago R$"+sale.getPaidValue());
                          }
                        }
                        mPrinter.setUnderline();
                        mPrinter.printText("Desconto   R$ "+sale.getDiscount());
                        mPrinter.setNormal();
                        mPrinter.printText("Total      R$"+sale.getToReceive());
                        mPrinter.feedPaper();
                        mPrinter.finish();
                    } else {
                        FirebaseCrashlytics.getInstance().recordException(new Throwable("Venda sem produtos, ID da venda: " + sale.getSaleId()));
                        Toast.makeText(mContext, "Erro ao imprimir, venda sem produtos!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailed() {
                    Toast.makeText(mContext, "Erro ao conectar com a impressora!", Toast.LENGTH_SHORT).show();
                }

            });
        }catch(NoSuchElementException ex){
            FirebaseCrashlytics.getInstance().recordException(ex);
            Toast.makeText(mContext, "Erro ao imprimir recibo!", Toast.LENGTH_SHORT).show();

        }
    }

    private String calculateTotalProduct(String productPrice, int quantity) {
        BigDecimal price = new BigDecimal(productPrice);
        BigDecimal qtt = new BigDecimal(quantity);

        return price.multiply(qtt).toString();
    }

    public void printSalesList(final List<Sale> listSales){
        BluetoothDevice mBtDevice = btAdapter.getBondedDevices().iterator().next();
        mPrinter = new BluetoothPrinter(mBtDevice);
        mPrinter.connectPrinter(new BluetoothPrinter.PrinterConnectListener(){
            @Override
            public void onConnected() {
                mPrinter.setAlign(BluetoothPrinter.ALIGN_CENTER);
                mPrinter.setBold(true);
                mPrinter.setBigSize();
                mPrinter.printText("VENDAS A RECEBER");
                mPrinter.printDoubleLine();
                mPrinter.setNormal();
                mPrinter.setAlign(BluetoothPrinter.ALIGN_LEFT);
                if(listSales.size()>0){
                    for(int i=0;i<listSales.size();i++){
                        final int finalI = i;
                        clientsRef.document(listSales.get(i).getClientId()).get().addOnCompleteListener(task -> {
                            Client c = Objects.requireNonNull(task.getResult()).toObject(Client.class);
                            assert c != null;
                            mPrinter.printText("Cliente:"+listSales.get(finalI).getClientName());
                            mPrinter.printText("Fone:"+listSales.get(finalI).getClientPhone());
                            mPrinter.printText("Rua:"+listSales.get(finalI).getClientStreet());
                            listSales.get(finalI).getClientComplement();
                            if(!listSales.get(finalI).getClientComplement().isEmpty()) {
                                mPrinter.printText("Complemento:" + listSales.get(finalI).getClientComplement());
                            }
                            mPrinter.printText("Bairro:"+listSales.get(finalI).getClientNeighborhood());
                            mPrinter.printText("Cidade:"+listSales.get(finalI).getClientCity());
                            mPrinter.printText("Valor a receber: R$"+listSales.get(finalI).getToReceive());
                            mPrinter.printText("Data da Venda:"+ FormatDate.INSTANCE.formatDateToString(listSales.get(finalI).getSaleDate()));
                            mPrinter.printLine();
                        });

                    }}else{
                    mPrinter.printText("NENHUM VALOR A RECEBER!");
                }
                mPrinter.feedPaper();
            }
            @Override
            public void onFailed() {
                Toast.makeText(mContext, "Conexão com a impressora falhou!", Toast.LENGTH_SHORT).show();
            }

        });
    }

}

