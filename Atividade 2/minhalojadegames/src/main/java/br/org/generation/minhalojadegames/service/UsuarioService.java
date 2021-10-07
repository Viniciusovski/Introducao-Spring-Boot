package br.org.generation.minhalojadegames.service;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.org.generation.minhalojadegames.model.Usuario;
import br.org.generation.minhalojadegames.model.UsuarioLogin;
import br.org.generation.minhalojadegames.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public List<Usuario> listarUsuario(){
		return usuarioRepository.findAll();
	}
	
	public Optional<Usuario> cadastrarUsuario(Usuario usuario){
		if(usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) {
			return Optional.empty();
		}
		
		//Criptografar senha
		BCryptPasswordEncoder enconder = new BCryptPasswordEncoder();
		
		String senhaEnconder = enconder.encode(usuario.getSenha());
		
		usuario.setSenha(senhaEnconder);
		
		return Optional.of(usuarioRepository.save(usuario));
	}
	
	public Optional<UsuarioLogin> loginUsuario(Optional<UsuarioLogin> usuarioLogin){
		BCryptPasswordEncoder enconder = new BCryptPasswordEncoder();
		
		Optional<Usuario> usuario = usuarioRepository
				.findByUsuario(usuarioLogin.get().getUsuario());
		
		if(usuario.isPresent()) {
			if(enconder.matches(usuarioLogin.get().getSenha(), usuario.get().getSenha())) {
				//Formação do Token
				String auth = usuarioLogin.get().getUsuario() + usuarioLogin.get().getSenha();
				
				byte[] encodeAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
				
				String authHeader = "Basic " + new String(encodeAuth);
				
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setSenha(usuario.get().getSenha());
				usuarioLogin.get().setToken(authHeader);
				
				return usuarioLogin;
			}
		}
		return Optional.empty();
	}
	 

}
