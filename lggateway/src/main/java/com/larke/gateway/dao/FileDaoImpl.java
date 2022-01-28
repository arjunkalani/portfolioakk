package com.larke.gateway.dao;

import java.io.InputStream;


import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.larke.gateway.mapper.UserMapper;
import com.larke.gateway.model.File;
import com.larke.gateway.model.Role;
import com.larke.gateway.model.User;

@ComponentScan(basePackages = "com.larke.gateway")
@Repository
public class FileDaoImpl implements FileDao {

	static final Logger LOGGER = LoggerFactory.getLogger(FileDaoImpl.class);

	@Autowired
	private static JdbcTemplate jdbcTemp;

	public FileDaoImpl(DataSource dataSource) {
		jdbcTemp = new JdbcTemplate(dataSource);
	}

	@Override
	public void updateDbWithFileInfo(String id, String name, String url, Long size, long owner) {
		String insertQuery = "insert into file (id, name, type, filecontent, url, size, owner) values (?, ?, ?, ?, ?,?,?)";
		jdbcTemp.update(insertQuery,  id, name, url, size, owner);
	}
	

	private static final class SingleFileMapper implements RowMapper<File> {
		public File mapRow(ResultSet rs, int rowNum) throws SQLException {
			File file = new File();
			file.setId(rs.getString("id"));
			file.setName(rs.getString("name"));
			file.setType(rs.getString("type"));
			file.setFilecontent(rs.getBytes("filecontent"));
			file.setUrl(rs.getString("url"));
			file.setSize(rs.getLong("size"));
			file.setOwner(rs.getLong("owner"));
			return file;
		}
	}
	
	public List<File> listAllFilesDAO() {
		String selectQuery = "SELECT * from File";
		return jdbcTemp.query(selectQuery, new SingleFileMapper());
	}
	
	public File findByFileName(String fileName) {
		String selectQuery = "SELECT name from File where name = ?";
		return jdbcTemp.queryForObject(selectQuery, new SingleFileMapper(), new Object[] { fileName });
	}

}
