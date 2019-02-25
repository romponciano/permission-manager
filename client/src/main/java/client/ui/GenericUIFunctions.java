package client.ui;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import client.exceptions.UICheckFieldException;
import client.ui.UIEnums.FORM_CONTEXT;
import common.exceptions.ServerServiceException;
import common.model.BusinessEntity;

public interface GenericUIFunctions {

	/**
	 * Método para pegar os campos de cada form e transformar em um objeto da classe
	 * a ser trabalhada.
	 * 
	 * @return - um objeto BusinessEntity (de onde todos os modelos são extendidos),
	 *         porém com os campos específicos de cada classe.
	 */
	public abstract BusinessEntity createObjToBeSaved();

	/**
	 * Método responsável por setar contexto do momento. Por exemplo: habilitar e
	 * desabilitar os campos e botões corretos para edição de um item;
	 * 
	 * @param context           - Contexto desejado: Edição, Criação ou Proibido
	 *                          (desabilita e limpa campos)
	 * @param selectedRowToEdit - <b>OBRIGRATÓRIO SE<b> for contexto de Edição.
	 *                          Passar linha que foi selecionada pelo usuário na
	 *                          tabela de resultados
	 */
	public default void setContext(FORM_CONTEXT context, Integer... selectedRowToEdit) {
		if (context.equals(FORM_CONTEXT.Criar))
			setContextoCriar();
		else if (context.equals(FORM_CONTEXT.Editar))
			setContextoEditar((int) selectedRowToEdit[0]);
		else if (context.equals(FORM_CONTEXT.Proibido))
			setContextoProibido();
	}

	/**
	 * Método para setar campos do contexto de edição. Ou seja, contexto onde o
	 * usuário irá editar dados de uma dada linha selecionada.
	 * 
	 * @param selectedRowToEdit - linha selecionada pelo usuário, da tabela de
	 *                          resultados, para edição
	 */
	public void setContextoEditar(int selectedRowToEdit);

	/**
	 * Método para setar campos do contexto proibido. Ou seja, contexto onde o
	 * usuário não consegue clicar ou edição nenhum campo do formulário
	 */
	public void setContextoProibido();

	/**
	 * Método para setar campos do contexto de criação. Ou seja, contexto onde o
	 * usuário irá inserir um novo item e, portanto, preencher os campos do
	 * formulário
	 */
	public void setContextoCriar();

	/**
	 * Método para carregar todos os itens de um determinado elemento na tabela.
	 * 
	 * @throws NotBoundException      - erro ao 'renderizar' component
	 * @throws ServerServiceException - erro vindo do servidor
	 * @throws RemoteException        - erro de comunicação com o servidor
	 */
	public void loadData() throws RemoteException, ServerServiceException, NotBoundException;

	/**
	 * Método para iniciar os listeners padrões das abas
	 */
	public void initListeners();

	public default void actionNewItem() {
		try {
			loadData();
		} catch (RemoteException | ServerServiceException | NotBoundException e) {
		} // nesse caso não fazer nada
		setContextoCriar();
	}

	public default void actionSaveNewItem() {
		try {
			if (checkFieldsOnCreate()) {
				BusinessEntity objToSave = createObjToBeSaved();
				objToSave.setDataCriacao(new Date());
				realizarCreate(objToSave);
				setContext(FORM_CONTEXT.Proibido);
				loadData();
			}
		} catch (ServerServiceException | RemoteException | NotBoundException | UICheckFieldException e) {
			dealWithError(e);
		}
	}

	public default void actionUpdateItem() {
		try {
			if (checkFieldsOnCreate()) {
				BusinessEntity objToSave = createObjToBeSaved();
				objToSave.setId(getIdToUpdateItem());
				objToSave.setDataUltimaModificacao(new Date());
				realizarUpdate(objToSave);
				setContext(FORM_CONTEXT.Proibido);
				loadData();
			}
		} catch (ServerServiceException | RemoteException | NotBoundException | UICheckFieldException e) {
			dealWithError(e);
		}
	}

	/**
	 * Método para checar se os campos estão válidos antes de salvar novo item
	 * 
	 * @throws UICheckFieldException - se algum campo não passar na validação
	 */
	public boolean checkFieldsOnCreate() throws UICheckFieldException;

	public default void actionRemoveItem() {
		try {
			realizarDelete(getIdToRemoveItem());
			loadData();
		} catch (ServerServiceException | RemoteException | NotBoundException e) {
			dealWithError(e);
		}
		setContext(FORM_CONTEXT.Proibido);
	}

	public default void actionSearchItems(String att, String searchString) {
		try {
			popularTabelaResultado(realizarBusca(att, searchString));
		} catch (ServerServiceException | RemoteException | NotBoundException e) {
			dealWithError(e);
		}
		setContext(FORM_CONTEXT.Proibido);
	}

	public default void actionCancel() {
		setContext(FORM_CONTEXT.Proibido);
	}

	/**
	 * Método para realizar a busca, onde cada aba chama o seu search do server.
	 * Neste método o objeto é convertido para sua classe específica
	 * 
	 * @param atributo - atributo selecionado na combobox de consulta
	 * @param termo    - termo a ser procurado daquele atributo. Ex: atributo LIKE
	 *                 termo
	 * @return - Lista contendo os valores encontrados da classe que realizou a
	 *         busca
	 * @throws RemoteException
	 * @throws ServerServiceException
	 * @throws NotBoundException
	 */
	public List<? extends BusinessEntity> realizarBusca(String atributo, String termo)
			throws RemoteException, ServerServiceException, NotBoundException;

	public void popularTabelaResultado(List<? extends BusinessEntity> resultadoConsulta);

	/**
	 * Método para realizar o delete, onde cada aba chama o seu delete do server.
	 * Neste método o objeto é convertido para sua classe específica
	 * 
	 * @param id - id no banco do objeto a ser deleteado
	 * @throws RemoteException
	 * @throws ServerServiceException
	 * @throws NotBoundException
	 */
	public abstract void realizarDelete(Long id) throws RemoteException, ServerServiceException, NotBoundException;

	public Long getIdToRemoveItem();

	/**
	 * Método para realizar o create, onde cada aba chama o seu create do server.
	 * Neste método o objeto é convertido para sua classe específica
	 * 
	 * @param objToSave - obj genérico que extendeu BusinessEntity e que representa
	 *                  o obj a ser salvo no bd
	 * @throws RemoteException
	 * @throws ServerServiceException
	 * @throws NotBoundException
	 */
	public void realizarCreate(BusinessEntity objToSave)
			throws RemoteException, ServerServiceException, NotBoundException;

	public Long getIdToUpdateItem();

	/**
	 * Método para realizar o update, onde cada aba chama o seu update do server.
	 * Neste método o objeto é convertido para sua classe específica
	 * 
	 * @param objToSave - obj genérico que extendeu BusinessEntity e que representa
	 *                  o obj a ser salvo no bd
	 * @throws RemoteException
	 * @throws ServerServiceException
	 * @throws NotBoundException
	 */
	public void realizarUpdate(BusinessEntity objToSave)
			throws RemoteException, ServerServiceException, NotBoundException;

	public default void dealWithError(Object ex) {
		if (ex instanceof UICheckFieldException)
			showInfoMessage(((UICheckFieldException) ex).getMessage());
		else if (ex instanceof ServerServiceException)
			showErrorMessage(((ServerServiceException) ex).getMessage());
		else if (ex instanceof RemoteException)
			showErrorMessage(((RemoteException) ex).getMessage());
		else if (ex instanceof NotBoundException)
			showErrorMessage(((NotBoundException) ex).getMessage());
	}

	/**
	 * Método para exibir DialogBox de algum <b>erro</b>.
	 * 
	 * @param msg - mensagem a ser exibida no DialogBox
	 */
	public abstract void showInfoMessage(String message);

	/**
	 * Método para exibir DialogBox de algum <b>erro</b>.
	 * 
	 * @param msg - mensagem a ser exibida no DialogBox
	 */
	public abstract void showErrorMessage(String message);
}
