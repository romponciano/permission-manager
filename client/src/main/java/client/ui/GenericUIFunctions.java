package client.ui;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import client.exceptions.UICheckFieldException;
import client.ui.UIEnums.ABAS;
import client.ui.UIEnums.FORM_CONTEXT;
import common.exceptions.ServerServiceException;
import common.model.BusinessEntity;

/**
 * Interface criada para definir métodos que são compartilhados entre
 * todas as UIs e abas do sistema. 
 * @author romuloponciano
 *
 */
public interface GenericUIFunctions {

	/**
	 * Método para pegar os campos de cada form e transformar em um objeto da classe
	 * a ser trabalhada.
	 * 
	 * @return - um objeto BusinessEntity (de onde todos os modelos são extendidos),
	 *         porém com os campos específicos de cada classe.
	 */
	public BusinessEntity createObjToBeSaved();
	
	/**
	 * Método para exibir DialogBox de algum <b>erro</b>.
	 * 
	 * @param msg - mensagem a ser exibida no DialogBox
	 */
	public boolean askConfirmation(String msg);
	
	/**
	 * Método para popular tabela de resultados de busca com lista de usuários
	 * 
	 * @param users - Lista contendo os usuários a serem apresentados na tabela
	 * @param tipo  - tipo para gerar Header da tabela
	 */
	public void populateTableAllItems(List<? extends BusinessEntity> objs);

	/**
	 * Método para converter escolha de busca da combobox para atributo que será
	 * utilizado na consulta ao banco.
	 * 
	 * @param cmbChoice - escolha do usuário
	 * @return - String do atributo utilizado no banco
	 */
	public default String convertComboChoiceToDBAtributte(String cmbChoice, ABAS aba) {
		if(aba.equals(ABAS.Usuário)) {
			if(cmbChoice.equals(UIEnums.FILTROS_USUARIO.Nome.toString())) return UIEnums.FILTROS_USUARIO.Nome.getValue();
			if(cmbChoice.equals(UIEnums.FILTROS_USUARIO.Login.toString())) return UIEnums.FILTROS_USUARIO.Login.getValue();
			if(cmbChoice.equals(UIEnums.FILTROS_USUARIO.Gerência.toString())) return UIEnums.FILTROS_USUARIO.Gerência.getValue();
			if(cmbChoice.equals(UIEnums.FILTROS_USUARIO.Status.toString())) return UIEnums.FILTROS_USUARIO.Status.getValue();
		}
		else if(aba.equals(ABAS.Funcionalidade)) {
			if(cmbChoice.equals(UIEnums.FILTROS_FUNCIONALIDADE.Nome.toString())) return UIEnums.FILTROS_FUNCIONALIDADE.Nome.getValue();
			if(cmbChoice.equals(UIEnums.FILTROS_FUNCIONALIDADE.Descrição.toString())) return UIEnums.FILTROS_FUNCIONALIDADE.Descrição.getValue();
			if(cmbChoice.equals(UIEnums.FILTROS_FUNCIONALIDADE.Data.toString())) return UIEnums.FILTROS_FUNCIONALIDADE.Data.getValue();
			if(cmbChoice.equals(UIEnums.FILTROS_FUNCIONALIDADE.Plugin.toString())) return UIEnums.FILTROS_FUNCIONALIDADE.Plugin.getValue();
		}
		else if(aba.equals(ABAS.Perfil)) {
			if(cmbChoice.equals(UIEnums.FILTROS_PERFIL.Nome.toString())) return UIEnums.FILTROS_PERFIL.Nome.getValue();
			if(cmbChoice.equals(UIEnums.FILTROS_PERFIL.Descrição.toString())) return UIEnums.FILTROS_PERFIL.Descrição.getValue();
			if(cmbChoice.equals(UIEnums.FILTROS_PERFIL.Data.toString())) return UIEnums.FILTROS_PERFIL.Data.getValue();
		}
		else if(aba.equals(ABAS.Plugin)) {
			if(cmbChoice.equals(UIEnums.FILTROS_PLUGIN.Nome.toString())) return UIEnums.FILTROS_PLUGIN.Nome.getValue();
			if(cmbChoice.equals(UIEnums.FILTROS_PLUGIN.Descrição.toString())) return UIEnums.FILTROS_PLUGIN.Descrição.getValue();
			if(cmbChoice.equals(UIEnums.FILTROS_PLUGIN.Data.toString())) return UIEnums.FILTROS_PLUGIN.Data.getValue();
		}
		return "";
	}
	
	/**
	 * Método para gerar items dos atributos que vão compor a combobox
	 * 
	 * @return - vetor de string contendo os items
	 */
	public void populateConsultComboBox();
	
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
	 * Método responsável por preencher formulário de edição com os valores de uma
	 * linha que foi selecionada da tabela de resultados. Como alguns campos são
	 * preenchidos com valores vindos do banco, então este método pode lançar
	 * exceções vindas do server.
	 * 
	 * @param selectedRowToEdit - linha selecionada
	 * @throws RemoteException
	 * @throws ServerServiceException
	 * @throws NotBoundException
	 */
	public void fillFormToEdit(int selectedRow) throws RemoteException, ServerServiceException, NotBoundException;

	/**
	 * Método para iniciar painel de formulário de cada aba
	 */
	public void initPnlForm();
	
	/**
	 * Método responsável por limpar formulário de edição/criação
	 */
	public void clearForm();

	/**
	 * Método responsável por habilitar ou desabilitar os campos de formulário
	 * 
	 * @param value - boolean: true para habilitar; false para desabilitar;
	 */
	public void setEnabledForm(boolean b);

	/**
	 * Método para iniciar os listeners padrões das abas
	 */
	public void initListeners();

	/**
	 * Método responsável por atualizar os dados e setar contexto de criação
	 * de novo item. Este método <b>não</b> é responsável por salvar o item no banco.
	 */
	public default void actionNewItem() {
		try {
			loadData();
		} catch (RemoteException | ServerServiceException | NotBoundException e) {
		} // nesse caso não fazer nada
		setContextoCriar();
	}

	/**
	 * Método responsável pela criação de um novo item no banco. 
	 */
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

	/**
	 * Método responsável pela atualização de um item no banco
	 */
	public default void actionUpdateItem() {
		try {
			if (checkFieldsOnCreate()) {
				BusinessEntity objToSave = createObjToBeSaved();
				objToSave.setId(getSelectedItemId());
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
	 * @throws UICheckFieldException - se algum campo não passar na validação
	 */
	public boolean checkFieldsOnCreate() throws UICheckFieldException;

	/**
	 * Método responsável pela ação de remover um item do banco de dados
	 */
	public default void actionRemoveItem() {
		try {
			realizarDelete(getSelectedItemId());
			loadData();
		} catch (ServerServiceException | RemoteException | NotBoundException e) {
			dealWithError(e);
		}
		setContext(FORM_CONTEXT.Proibido);
	}

	/**
	 * Método responsável por get o id de um item selecionado na 
	 * tabela principal de cada aba
	 * @return - id do item selecionado
	 */
	public abstract Long getSelectedItemId();

	/**
	 * Método responsável por executar a ação de busca de itens pelo campo
	 * e string de buscas escolhidos e definidos, respectivamente, pelo usuário.
	 * O método não retorna o resultado da busca, pois ele popula diretamente 
	 * na tabela principal da aba.
	 * @param att - atributo escolhido
	 * @param searchString - string de busca definida 
	 */
	public default void actionSearchItems(String att, String searchString) {
		try {
			populateTableAllItems(realizarBusca(att, searchString));
		} catch (ServerServiceException | RemoteException | NotBoundException e) {
			dealWithError(e);
		}
		setContext(FORM_CONTEXT.Proibido);
	}

	/**
	 * Método responsável pela ação dos botões de cancelar edição e criação de item
	 */
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

	/**
	 * Método responsável por lidar com erros do sistema. Este método checa
	 * se é um erro esperado e conhecido. Caso seja, ele irá exibir a
	 * informação desse erro para o usuário.
	 * @param ex - exception que ocorreu
	 */
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
	 * Método para exibir DialogBox de alguma <b>informação</b>.
	 * @param msg - mensagem a ser exibida no DialogBox
	 */
	public abstract void showInfoMessage(String message);

	/**
	 * Método para exibir DialogBox de algum <b>erro</b>.
	 * @param msg - mensagem a ser exibida no DialogBox
	 */
	public abstract void showErrorMessage(String message);
}
